package net.killarexe.littlerage.engine;

import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.imGui.ImGuiLayer;
import net.killarexe.littlerage.engine.input.*;
import net.killarexe.littlerage.engine.observers.*;
import net.killarexe.littlerage.engine.observers.events.*;
import net.killarexe.littlerage.engine.renderer.*;
import net.killarexe.littlerage.engine.scene.*;
import net.killarexe.littlerage.engine.util.*;
import org.lwjgl.Version;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.Callbacks.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

public class Window implements Observer {

    private static int currentSceneIndex = -1;
    private static Window window = null;
    private static Scene currentScene;
    //public float r, g, b, a;
    private int width, height;
    private long glfwWindow;
    private String title;
    private String VER = "0.1a";
    private ImGuiLayer imGuiLayer;
    private Framebuffer framebuffer;
    private PickingTexture pickingTexture;
    private DiscordController controller = new DiscordController("Playing Little Rage Maker v" + VER, "Editing...", null, null, "848548611423207446", null);;

    private static Logger LOGGER = new Logger(Window.class);
    private Logger logger = LOGGER;

    private boolean runtimePlaying = false;

    private Window(){
        this.width = 1680;
        this.height = 1050;
        this.title = "Little Rage Engine v" + this.VER;

        EventSystem.addObserver(this);
    }

    public static Window getInstance(){
        if (Window.window == null){
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run(){
        LOGGER.info("Running Little Rage " + this.VER);
        LOGGER.info("Running With LWJGL" + Version.getVersion());
        init();
        LOGGER.info("Little Rage Engine v" + this.VER + " Initialized!");
        loop();

        stop();
    }

    public void init(){
        //Setup Error Callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Init GLFW
        if(!glfwInit()){
            LOGGER.error("Unable/Failed to load GLFW!");
        }

        //Config GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //Create Window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            LOGGER.error("Unable/Failed to create Window!");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) ->{
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        //Make OpenGL
        glfwMakeContextCurrent(glfwWindow);
        //V-Sync
        glfwSwapInterval(1);
        //Set Window Visible
        glfwShowWindow(glfwWindow);
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.framebuffer = new Framebuffer(width, height);
        this.pickingTexture = new PickingTexture(width, height);
        glViewport(0,0, width, height);

        this.imGuiLayer = new ImGuiLayer(glfwWindow, pickingTexture);
        this.imGuiLayer.initImGui();

        Window.changeScene(new LevelEditiorSceneInitializer());
    }

    public void loop(){

        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;

        Shader defaultShader = AssetPool.getShader("assets/shaders/default.glsl");
        Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");

        while(!glfwWindowShouldClose(glfwWindow)){

            //Poll Events
            glfwPollEvents();

            //Render pass 1. Render to picking texture
            glDisable(GL_BLEND);
            pickingTexture.enableWriting();
            glViewport(0,0,width, height);
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer.bindShader(pickingShader);
            currentScene.render();

            pickingTexture.disableWriting();
            glEnable(GL_BLEND);

            //Render pass 2. Render actual game
            DebugDraw.beginFrame();

            this.framebuffer.bind();
            glClearColor(1, 1, 1, 1);
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt >= 0){
                DebugDraw.draw();
                Renderer.bindShader(defaultShader);
                if(runtimePlaying) {
                    currentScene.update(dt);
                }else{
                    currentScene.editorUpdate(dt);
                }
                currentScene.render();
            }
            this.framebuffer.unbind();

            this.imGuiLayer.update(dt, currentScene);
            glfwSwapBuffers(glfwWindow);
            MouseListener.endFrame();

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
        controller.stop();
        currentScene.save();
        System.exit(0);
    }

    public static void changeScene(SceneInitializer initializer){

        if(currentScene != null){
            currentScene.destroy();
        }

        getImGuiLayer().getPropertiesWindow().setActiveGameObject(null);
        currentScene = new Scene(initializer);
        currentScene.load();
        currentScene.init();
        currentScene.start();
    }

    public void stop(){
        //Free Memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminaite
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    @Override
    public void onNotify(GameObject object, Event event) {
        switch (event.type){
            case GameEngineStartPlay:
                logger.info("Start Playing!");
                this.runtimePlaying = true;
                currentScene.save();
                changeScene(new LevelEditiorSceneInitializer());
                break;
            case GameEngineStopPlay:
                logger.info("Stop Playing!");
                this.runtimePlaying = false;
                Window.changeScene(new LevelEditiorSceneInitializer());
                break;
            case UserEvent:
                break;
            case LoadLevel:
                changeScene(new LevelEditiorSceneInitializer());
                break;
            case SaveLevel:
                currentScene.save();
                break;
        }
    }

    //Setters
    public static void setWidth(int newWidth){
        getInstance().width = newWidth;
    }
    public static void setHeight(int newHeight){
        getInstance().height = newHeight;
    }

    //Getters
    public static Scene getScene(){
        return getInstance().currentScene;
    }
    public static int getWidth(){return getInstance().width;}
    public static int getHeight(){return getInstance().height;}
    public static Framebuffer getFramebuffer(){return getInstance().framebuffer;}
    public static float getTargetAspect169(){return 16.0f / 9.0f;}
    public static float getTargetAspect1610(){return 16.0f / 10.0f;}
    public static ImGuiLayer getImGuiLayer(){return getInstance().imGuiLayer;}
    public static DiscordController getController() { return getInstance().controller; }
}

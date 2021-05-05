package net.killarexe.littlerage.engine;

import net.killarexe.littlerage.engine.imGui.ImGuiLayer;
import net.killarexe.littlerage.engine.input.*;
import net.killarexe.littlerage.engine.renderer.DebugDraw;
import net.killarexe.littlerage.engine.renderer.Framebuffer;
import net.killarexe.littlerage.engine.scene.*;
import net.killarexe.littlerage.engine.util.*;
import net.killarexe.littlerage.scene.LevelScene;
import org.lwjgl.Version;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.Callbacks.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

public class Window {

    private static int currentSceneIndex = -1;
    private static Window window = null;
    private static Scene currentScene;
    public float r, g, b, a;
    private int width, height;
    private long glfwWindow;
    private String title;
    private String VER;
    private ImGuiLayer imGuiLayer;
    private Framebuffer framebuffer;

    private static Logger LOGGER = new Logger(Window.class);

    private Window(){
        this.width = 1280;
        this.height = 720;
        this.VER = "0.1a";
        this.title = "Little Rage " + this.VER;
        r = 1;
        b = 1;
        g = 1;
        a = 1;
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
        LOGGER.info("Little Rage Initialized!");
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

        this.imGuiLayer = new ImGuiLayer(glfwWindow);
        this.imGuiLayer.initImGui();

        this.framebuffer = new Framebuffer(1680, 1050);

        Window.changeScene(0);
    }

    public void loop(){
        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow)){
            //Poll Events
            glfwPollEvents();

            DebugDraw.beginFrame();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            this.framebuffer.bind();
            if(dt >= 0){
                DebugDraw.draw();
                currentScene.update(dt);
            }
            this.framebuffer.unbind();

            this.imGuiLayer.update(dt, currentScene);
            glfwSwapBuffers(glfwWindow);

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
        currentScene.saveExit();
    }

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditiorScene();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                LOGGER.fatal("Unknown Scene: " + newScene);
                break;
        }

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

    public static void setWidth(int newWidth){
        getInstance().width = newWidth;
    }

    public static void setHeight(int newHeight){
        getInstance().height = newHeight;
    }

    public static Scene getScene(){
        return getInstance().currentScene;
    }
    public static int getWidth(){return getInstance().width;}
    public static int getHeight(){return getInstance().height;}
}

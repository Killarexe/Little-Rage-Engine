package net.killarexe.littlerage.engine;

import net.killarexe.littlerage.engine.input.*;
import net.killarexe.littlerage.engine.scene.*;
import net.killarexe.littlerage.engine.util.*;
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
    private Logger LOGGER = new Logger(getClass());

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

        //Free Memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminaite
        glfwTerminate();
        glfwSetErrorCallback(null).free();
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

        //Make OpenGL
        glfwMakeContextCurrent(glfwWindow);
        //V-Sync
        glfwSwapInterval(1);
        //Set Window Visible
        glfwShowWindow(glfwWindow);
        GL.createCapabilities();

        Window.changeScene(0);
    }

    public void loop(){
        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow)){
            //Poll Events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt >= 0)currentScene.update(dt);

            glfwSwapBuffers(glfwWindow);

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditiorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false : "Unknown Scene: " + newScene;
                break;
        }
    }

    public static Scene getScene(){
        return getInstance().currentScene;
    }

}

package net.killarexe.littlerage.engine.input;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener instance;
    private boolean KeyPressed[] = new boolean[350];
    private boolean keyBeginPress[] = new boolean[350];

    private KeyListener(){

    }

    public static KeyListener getInstance(){
        if(KeyListener.instance == null){
           KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if(action == GLFW_PRESS){
            getInstance().KeyPressed[key] = true;
        }else if(action == GLFW_RELEASE){
            getInstance().KeyPressed[key] = false;
        }
    }

    public static boolean keyBeginPress(int keyCode) {
        boolean result = getInstance().keyBeginPress[keyCode];
        if (result) {
            getInstance().keyBeginPress[keyCode] = false;
        }
        return result;
    }

    public static boolean isKeyPressed(int keyCode){
        return getInstance().KeyPressed[keyCode];
    }
}

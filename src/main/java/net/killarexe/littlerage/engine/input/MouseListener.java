package net.killarexe.littlerage.engine.input;

import net.killarexe.littlerage.engine.Window;
import net.killarexe.littlerage.engine.gameObject.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY, worldX, worldY, lastWorldX, lastWorldY;
    private boolean mouseButtonPressed[] = new boolean[9];
    private boolean isDragging;

    private int mouseButtonDown = 0;

    private Vector2f gameViewportPos = new Vector2f();
    private Vector2f gameViewportSize = new Vector2f();

    private MouseListener(){
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static MouseListener getInstance(){
        if(MouseListener.instance == null){
            instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos){

        if(getInstance().mouseButtonDown > 0){
            getInstance().isDragging = true;
        }

        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
        getInstance().lastWorldX = getInstance().worldX;
        getInstance().lastWorldY = getInstance().worldY;
        getInstance().xPos = xPos;
        getInstance().yPos = yPos;
        calcOrthoX();
        calcOrthoY();
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if(action == GLFW_PRESS) {
            getInstance().mouseButtonDown++;
            if(button < getInstance().mouseButtonPressed.length){
                getInstance().mouseButtonPressed[button] = true;
            }
        }else if(action == GLFW_RELEASE){
            getInstance().mouseButtonDown--;
            if(button < getInstance().mouseButtonPressed.length){
                getInstance().mouseButtonPressed[button] = false;
                getInstance().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        getInstance().scrollX = xOffset;
        getInstance().scrollY = yOffset;
    }

    public static void endFrame(){
        getInstance().scrollX = 0;
        getInstance().scrollY = 0;
        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
        getInstance().lastWorldX = getInstance().worldX;
        getInstance().lastWorldY = getInstance().worldY;
    }

    public static float getScreenX(){
        float currentX = getX() - getInstance().gameViewportPos.x;
        currentX = (currentX / getInstance().gameViewportSize.x) * Window.getWidth();
        return currentX;
    }
    public static float getScreenY(){
        float currentY = getY() - getInstance().gameViewportPos.y;
        currentY = Window.getHeight() - ((currentY / getInstance().gameViewportSize.y) * Window.getHeight());
        return currentY;
    }

    public static void calcOrthoX(){
        float currentX = getX() - getInstance().gameViewportPos.x;
        currentX = (currentX / getInstance().gameViewportSize.x) * 2f - 1f;
        Vector4f tmp = new Vector4f(currentX,0,0,1);
        Camera camera = Window.getScene().camera();
        Matrix4f viewProjection = new Matrix4f();
        camera.getInverseView().mul(camera.getInverseProjection(), viewProjection);
        tmp.mul(viewProjection);

        getInstance().worldX = tmp.x;
    }

    public static void calcOrthoY(){
        float currentY = getY() - getInstance().gameViewportPos.y;
        currentY = -((currentY / getInstance().gameViewportSize.y) * 2f - 1f);
        Vector4f tmp = new Vector4f(0,currentY,0,1);
        Camera camera = Window.getScene().camera();
        Matrix4f viewProjection = new Matrix4f();
        camera.getInverseView().mul(camera.getInverseProjection(), viewProjection);
        tmp.mul(viewProjection);

        getInstance().worldY = tmp.y;
    }


    public static boolean mouseButtonDown(int button){
        if(button < getInstance().mouseButtonPressed.length){
            return getInstance().mouseButtonPressed[button];
        }else{
            return false;
        }
    }

    public static void setGameViewportPos(Vector2f gameViewportPos) { getInstance().gameViewportPos.set(gameViewportPos); }
    public static void setGameVeiwportSize(Vector2f gameVeiwportSize) { getInstance().gameViewportSize.set(gameVeiwportSize); }

    public static float getX(){
        return (float)getInstance().xPos;
    }
    public static float getY(){
        return (float)getInstance().yPos;
    }
    public static float getOrthoY(){ return (float)getInstance().worldY; }
    public static float getOrthoX(){ return (float)getInstance().worldX; }
    public static float getDx(){
        return (float)(getInstance().lastX - getInstance().xPos);
    }
    public static float getDy(){
        return (float)(getInstance().lastX - getInstance().xPos);
    }
    public static float getScrollX(){return (float)getInstance().scrollX;}
    public static float getScrollY(){
        return (float)getInstance().scrollY;
    }
    public static float getWorldDy() { return (float)(getInstance().lastWorldY - getInstance().worldY); }
    public static float getWorldDx() { return (float)(getInstance().lastWorldX - getInstance().worldX); }
    public static boolean isDragging(){
        return getInstance().isDragging;
    }
}

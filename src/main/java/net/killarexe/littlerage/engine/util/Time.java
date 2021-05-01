package net.killarexe.littlerage.engine.util;

import static org.lwjgl.glfw.GLFW.*;

public class Time {
    public static float timeStarted = (float)glfwGetTime();

    public static float getTime() { return (float)((glfwGetTime() - timeStarted) * 1E-9); }
}

package net.killarexe.littlerage.engine.editor;

import imgui.*;
import imgui.flag.ImGuiWindowFlags;
import net.killarexe.littlerage.engine.Window;
import net.killarexe.littlerage.engine.input.MouseListener;
import org.joml.Vector2f;

public class GameViewWindow {

    private static float leftX, rightX, topY, bottomY;

    public static void imgui(){
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPosForViewport(windowSize);

        ImGui.setCursorPos(windowPos.x, windowPos.y);

        ImVec2 topLeft = new ImVec2();
        ImGui.getCursorScreenPos(topLeft);
        topLeft.x -= ImGui.getScrollX();
        topLeft.y -= ImGui.getScrollY();
        leftX = topLeft.x;
        topY = topLeft.y;
        rightX = topLeft.x + windowSize.x;
        bottomY = topLeft.y + windowSize.y;

        int texId = Window.getFramebuffer().getTextureID();
        ImGui.image(texId, windowSize.x, windowSize.y, 0, 1, 1, 0);

        MouseListener.setGameViewportPos(new Vector2f(topLeft.x, topLeft.y));
        MouseListener.setGameVeiwportSize(new Vector2f(windowSize.x, windowSize.y));

        ImGui.end();
    }

    private static ImVec2 getLargestSizeForViewport(){
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspect169();
        if(aspectHeight > windowSize.y){
            // Switch to pillarbox mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspect169();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private static ImVec2 getCenteredPosForViewport(ImVec2 aspectSize){
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float viewportX = (windowSize.x/ 2.0f) - (aspectSize.x / 2.0f);
        float viewportY = (windowSize.y/ 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }

    public static boolean getWantCaptureMouse(){
        return MouseListener.getX() >= leftX && MouseListener.getX() <= rightX && MouseListener.getY() >= bottomY && MouseListener.getY() <= topY;
    }
}

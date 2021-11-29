package net.killarexe.littlerage.engine.imGui;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import net.killarexe.littlerage.engine.Window;
import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.gameObject.components.SpriteRenderer;
import net.killarexe.littlerage.engine.renderer.Sprite;

import java.util.*;

public class SceneHierarchyWindow {

    private static String type = "SceneHierarchy";

    public void imgui(){
        ImGui.begin("Scene Hierarchy");
        List<GameObject> gameObjects = Window.getScene().getGameObjects();
        int index = 0;
        for (GameObject obj: gameObjects) {
            if(!obj.isDoSerialization()){
                continue;
            }

            boolean treeNodeOpen = doTreeNode(obj, index);

            if(treeNodeOpen){
                ImGui.treePop();
            }
            index++;
        }
        ImGui.end();
    }
    private boolean doTreeNode(GameObject obj, int index){
        ImGui.pushID(index);
        boolean treeNodeOpen = ImGui.treeNodeEx(obj.name, ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.FramePadding | ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.SpanAvailWidth, obj.name);
        ImGui.popID();

        if(ImGui.beginDragDropSource()){
            ImGui.setDragDropPayloadObject(type, obj);
            ImGui.text(obj.name);
            SpriteRenderer renderer = obj.getComponents(SpriteRenderer.class);
            if(renderer != null) {
                ImGui.image(renderer.getTextureId(), 50, 50, renderer.getTexCoords()[0].x, renderer.getTexCoords()[0].y, renderer.getTexCoords()[2].x, renderer.getTexCoords()[2].y);
            }
            ImGui.endDragDropSource();
        }

        if(ImGui.beginDragDropTarget()){
            Object payloadObj = ImGui.acceptDragDropPayloadObject(type);
            if(payloadObj != null){
                if(payloadObj instanceof GameObject){
                    GameObject playerGo = (GameObject)payloadObj;
                    System.out.println("Ok.");
                }
            }
            ImGui.endDragDropTarget();
        }

        return treeNodeOpen;
    }
}

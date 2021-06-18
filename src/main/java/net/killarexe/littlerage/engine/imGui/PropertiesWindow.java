package net.killarexe.littlerage.engine.imGui;

import imgui.ImGui;
import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.gameObject.components.NonPickable;
import net.killarexe.littlerage.engine.input.MouseListener;
import net.killarexe.littlerage.engine.physics2d.components.Box2DCollider;
import net.killarexe.littlerage.engine.physics2d.components.CircleCollider;
import net.killarexe.littlerage.engine.physics2d.components.Rigidbody2D;
import net.killarexe.littlerage.engine.renderer.PickingTexture;
import net.killarexe.littlerage.engine.scene.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {

    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    private float debounce = 0.2f;

    public PropertiesWindow(PickingTexture pickingTexture) {
        this.pickingTexture = pickingTexture;
    }

    public void update(float dt, Scene currentScene) {
        debounce -= dt;

        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
            int x = (int)MouseListener.getScreenX();
            int y = (int)MouseListener.getScreenY();
            int gameObjectId = pickingTexture.readPixel(x, y);
            GameObject pickedObj = currentScene.getGameObject(gameObjectId);
            if (pickedObj != null && pickedObj.getComponents(NonPickable.class) == null) {
                activeGameObject = pickedObj;
            } else if (pickedObj == null && !MouseListener.isDragging()) {
                activeGameObject = null;
            }
            this.debounce = 0.2f;
        }
    }

    public void imgui() {
        if (activeGameObject != null) {
            ImGui.begin("Properties");

            if(ImGui.beginPopupContextWindow("ComponentAdder")){
                if(ImGui.menuItem("Add Rigidbody")){
                    if(activeGameObject.getComponents(Rigidbody2D.class) == null){
                        activeGameObject.addComponents(new Rigidbody2D());
                    }
                }

                if(ImGui.menuItem("Add Box Collider")){
                    if(activeGameObject.getComponents(Box2DCollider.class) == null && activeGameObject.getComponents(CircleCollider.class) == null){
                        activeGameObject.addComponents(new Box2DCollider());
                    }
                }

                if(ImGui.menuItem("Add Circle Collider")){
                    if(activeGameObject.getComponents(CircleCollider.class) == null && activeGameObject.getComponents(Box2DCollider.class) == null){
                        activeGameObject.addComponents(new CircleCollider());
                    }
                }

                ImGui.endPopup();
            }

            activeGameObject.imgui();
            ImGui.end();
        }
    }

    public void setActiveGameObject(GameObject activeGameObject) { this.activeGameObject = activeGameObject; }

    public GameObject getActiveGameObject() { return this.activeGameObject; }
}

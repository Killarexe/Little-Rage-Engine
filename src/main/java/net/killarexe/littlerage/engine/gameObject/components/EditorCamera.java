package net.killarexe.littlerage.engine.gameObject.components;

import net.killarexe.littlerage.engine.gameObject.Camera;
import static net.killarexe.littlerage.engine.input.KeyListener.*;
import static net.killarexe.littlerage.engine.input.MouseListener.*;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class EditorCamera extends Component{

    private float dragDebounce = 0.032f;

    private Camera levelEditorCamera;
    private Vector2f clickOrigin;

    private float lerpTime = 0f;
    private float dragSensitivity = 30f;
    private float scrollSensitivity = 0.1f;

    private int fps;

    private boolean isReset = false;

    public EditorCamera(Camera levelEditorCamera){
        this.levelEditorCamera = levelEditorCamera;
        this.clickOrigin = new Vector2f();
    }

    @Override
    public void editorUpdate(float dt) {
        fps = (int)(1.0f / dt);
        if(mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT) && dragDebounce > 0){
            this.clickOrigin = new Vector2f(getOrthoX(), getOrthoY());
            dragDebounce -= dt;
            return;
        }else if(mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT)){
            Vector2f mousePos = new Vector2f(getOrthoX(), getOrthoY());
            Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin);
            levelEditorCamera.pos.sub(delta.mul(dt).mul(dragSensitivity));
            this.clickOrigin.lerp(mousePos, dt);
        }

        if(dragDebounce <= 0.0f && !mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT)){
            dragDebounce = 0.1f;
        }

        if(getScrollY() != 0.0f){
            float addValue = (float)Math.pow(Math.abs(getScrollY()) * scrollSensitivity, 1 / levelEditorCamera.getZoom());
            addValue *= Math.signum(getScrollY());
            levelEditorCamera.addZoom(addValue);
        }

        if(isKeyPressed(GLFW_KEY_F5)){
            isReset = true;
        }

        if(isReset){
            levelEditorCamera.pos.lerp(new Vector2f(), lerpTime);
            levelEditorCamera.setZoom(this.levelEditorCamera.getZoom() + ((1.0f - levelEditorCamera.getZoom()) * lerpTime));

            this.lerpTime += 0.1f * dt;

            if(Math.abs(levelEditorCamera.pos.x) <= 5f && Math.abs(levelEditorCamera.pos.y) <= 5f){
                this.lerpTime = 0f;
                levelEditorCamera.pos.set(0f, 0f);
                this.levelEditorCamera.setZoom(1f);
                isReset = false;
            }
        }
    }
}

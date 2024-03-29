package net.killarexe.littlerage.engine.gameObject.components;

import net.killarexe.littlerage.engine.Window;
import net.killarexe.littlerage.engine.gameObject.GameObject;
import static net.killarexe.littlerage.engine.input.MouseListener.*;
import net.killarexe.littlerage.engine.util.Settings;

import static org.lwjgl.glfw.GLFW.*;

public class MouseControls extends Component{

    GameObject holdingObject = null;

    public void pickupObject(GameObject object){
        this.holdingObject = object;
        Window.getScene().addGameObjectToScene(object);
    }

    public void place(){
        this.holdingObject = null;
    }

    @Override
    public void editorUpdate(float dt) {
        if(holdingObject != null){
            holdingObject.transform.pos.x = getOrthoX();
            holdingObject.transform.pos.y = getOrthoY();
            holdingObject.transform.pos.x = (int)(holdingObject.transform.pos.x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH;
            holdingObject.transform.pos.y = (int)(holdingObject.transform.pos.y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;

            if(mouseButtonDown(GLFW_MOUSE_BUTTON_1)){
                place();
            }
        }
    }
}

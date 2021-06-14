package net.killarexe.littlerage.engine.gameObject.components;

import net.killarexe.littlerage.engine.Window;
import net.killarexe.littlerage.engine.editor.PropertiesWindow;
import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.gameObject.Prefabs;
import net.killarexe.littlerage.engine.input.MouseListener;
import net.killarexe.littlerage.engine.renderer.Sprite;
import org.joml.Vector2f;
import org.joml.Vector4f;
import static org.lwjgl.glfw.GLFW.*;

public class Gizmo extends Component{
    private Vector4f xAxisColor = new Vector4f(1, 0.3f, 0.3f, 1);
    private Vector4f xAxisColorHover = new Vector4f(1, 0, 0, 1);
    private Vector4f yAxisColor = new Vector4f(0.3f, 1, 0.3f, 1);
    private Vector4f yAxisColorHover = new Vector4f(0, 1, 0, 1);

    private GameObject xAxisObject;
    private GameObject yAxisObject;
    private SpriteRenderer xAxisSprite;
    private SpriteRenderer yAxisSprite;
    protected GameObject activeGameObject = null;

    private Vector2f xAxisOffset = new Vector2f(100, -5);
    private Vector2f yAxisOffset = new Vector2f(60, 61);

    private int gizmoWidth = 16;
    private int gizmoHeight = 48;

    protected boolean xAxisActive = false;
    protected boolean yAxisActive = false;

    private boolean using = false;

    private PropertiesWindow propertiesWindow;

    public Gizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        this.xAxisObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);
        this.yAxisObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);
        this.xAxisSprite = this.xAxisObject.getComponents(SpriteRenderer.class);
        this.yAxisSprite = this.yAxisObject.getComponents(SpriteRenderer.class);
        this.propertiesWindow = propertiesWindow;

        this.xAxisObject.addComponents(new NonPickable());
        this.yAxisObject.addComponents(new NonPickable());

        Window.getScene().addGameObjectToScene(this.xAxisObject);
        Window.getScene().addGameObjectToScene(this.yAxisObject);
    }

    @Override
    public void start() {
        this.xAxisObject.transform.rotation = 90;
        this.yAxisObject.transform.rotation = 180;
        this.xAxisObject.transform.zIndex = Integer.MAX_VALUE;
        this.yAxisObject.transform.zIndex = Integer.MAX_VALUE;
        this.xAxisObject.setDoSerialization(false);
        this.yAxisObject.setDoSerialization(false);
    }

    @Override
    public void update(float dt) {
        if (!using) return;

        this.activeGameObject = this.propertiesWindow.getActiveGameObject();
        if (this.activeGameObject != null) {
            this.setActive();
        } else {
            this.setInactive();
            return;
        }

        boolean xAxisHot = checkXHoverState();
        boolean yAxisHot = checkYHoverState();

        if ((xAxisHot || xAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            xAxisActive = true;
            yAxisActive = false;
        } else if ((yAxisHot || yAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            yAxisActive = true;
            xAxisActive = false;
        } else {
            xAxisActive = false;
            yAxisActive = false;
        }

        if (this.activeGameObject != null) {
            this.xAxisObject.transform.pos.set(this.activeGameObject.transform.pos);
            this.yAxisObject.transform.pos.set(this.activeGameObject.transform.pos);
            this.xAxisObject.transform.pos.add(this.xAxisOffset);
            this.yAxisObject.transform.pos.add(this.yAxisOffset);
        }
    }

    private void setActive() {
        this.xAxisSprite.setColor(xAxisColor);
        this.yAxisSprite.setColor(yAxisColor);
    }

    private void setInactive() {
        this.activeGameObject = null;
        this.xAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
        this.yAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
    }

    private boolean checkXHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if (mousePos.x <= xAxisObject.transform.pos.x &&
                mousePos.x >= xAxisObject.transform.pos.x - gizmoHeight &&
                mousePos.y >= xAxisObject.transform.pos.y &&
                mousePos.y <= xAxisObject.transform.pos.y + gizmoWidth) {
            xAxisSprite.setColor(xAxisColorHover);
            return true;
        }

        xAxisSprite.setColor(xAxisColor);
        return false;
    }

    private boolean checkYHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if (mousePos.x <= yAxisObject.transform.pos.x &&
                mousePos.x >= yAxisObject.transform.pos.x - gizmoWidth &&
                mousePos.y <= yAxisObject.transform.pos.y &&
                mousePos.y >= yAxisObject.transform.pos.y - gizmoHeight) {
            yAxisSprite.setColor(yAxisColorHover);
            return true;
        }

        yAxisSprite.setColor(yAxisColor);
        return false;
    }

    public void setUsing() {
        this.using = true;
    }

    public void setNotUsing() {
        this.using = false;
        this.setInactive();
    }
}

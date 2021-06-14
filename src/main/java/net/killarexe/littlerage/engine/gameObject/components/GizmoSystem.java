package net.killarexe.littlerage.engine.gameObject.components;

import net.killarexe.littlerage.engine.Window;
import net.killarexe.littlerage.engine.input.KeyListener;
import net.killarexe.littlerage.engine.renderer.SpriteSheet;

import static org.lwjgl.glfw.GLFW.*;

public class GizmoSystem extends Component{

    private SpriteSheet gizmos;
    private int usingGizmo = 0;

    public GizmoSystem(SpriteSheet gizmoSprites) {
        gizmos = gizmoSprites;
    }

    @Override
    public void start() {
        gameObject.addComponents(new TranslateGizmo(gizmos.getSprite(1),
                Window.getImGuiLayer().getPropertiesWindow()));
        gameObject.addComponents(new ScaleGizmo(gizmos.getSprite(2),
                Window.getImGuiLayer().getPropertiesWindow()));
    }

    @Override
    public void update(float dt) {
        if (usingGizmo == 0) {
            gameObject.getComponents(TranslateGizmo.class).setUsing();
            gameObject.getComponents(ScaleGizmo.class).setNotUsing();
        } else if (usingGizmo == 1) {
            gameObject.getComponents(TranslateGizmo.class).setNotUsing();
            gameObject.getComponents(ScaleGizmo.class).setUsing();
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_E)) {
            usingGizmo = 0;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_R)) {
            usingGizmo = 1;
        }
    }
}

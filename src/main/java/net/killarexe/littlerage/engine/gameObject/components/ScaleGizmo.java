package net.killarexe.littlerage.engine.gameObject.components;

import net.killarexe.littlerage.engine.editor.PropertiesWindow;
import net.killarexe.littlerage.engine.input.MouseListener;
import net.killarexe.littlerage.engine.renderer.Sprite;

public class ScaleGizmo extends Gizmo{

    public ScaleGizmo(Sprite scaleSprite, PropertiesWindow propertiesWindow) {
        super(scaleSprite, propertiesWindow);
    }

    @Override
    public void update(float dt) {
        if (activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.scale.x -= MouseListener.getWorldDx();
            } else if (yAxisActive) {
                activeGameObject.transform.scale.y -= MouseListener.getWorldDy();
            }
        }

        super.update(dt);
    }
}

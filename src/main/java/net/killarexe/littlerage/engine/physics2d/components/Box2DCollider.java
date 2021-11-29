package net.killarexe.littlerage.engine.physics2d.components;

import net.killarexe.littlerage.engine.gameObject.components.Component;
import net.killarexe.littlerage.engine.renderer.DebugDraw;
import org.joml.Vector2f;

public class Box2DCollider extends Collider {

    private Vector2f halfSize = new Vector2f(0.25f);
    private Vector2f origin = new Vector2f();

    public Vector2f getHalfSize() {
        return halfSize;
    }

    public Vector2f getOrigin() {
        return this.origin;
    }

    public void setHalfSize(Vector2f halfSize) {
        this.halfSize = halfSize;
    }

    @Override
    public void editorUpdate(float dt) {
        Vector2f center = new Vector2f(this.gameObject.transform.pos).add(this.offset);
        DebugDraw.addBox2D(center, this.halfSize, this.gameObject.transform.rotation);
    }
}

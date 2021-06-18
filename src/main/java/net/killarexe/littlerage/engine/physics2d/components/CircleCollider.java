package net.killarexe.littlerage.engine.physics2d.components;

import net.killarexe.littlerage.engine.gameObject.components.Component;

public class CircleCollider extends Collider {

    private float radius = 1f;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}

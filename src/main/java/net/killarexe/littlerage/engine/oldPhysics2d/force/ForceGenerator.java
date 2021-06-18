package net.killarexe.littlerage.engine.oldPhysics2d.force;

import net.killarexe.littlerage.engine.oldPhysics2d.rigidbody.Rigidbody2D;

public interface ForceGenerator {
    void updateForce(Rigidbody2D body, float dt);
}

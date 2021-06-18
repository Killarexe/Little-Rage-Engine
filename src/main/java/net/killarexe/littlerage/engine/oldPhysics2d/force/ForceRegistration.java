package net.killarexe.littlerage.engine.oldPhysics2d.force;

import net.killarexe.littlerage.engine.oldPhysics2d.rigidbody.Rigidbody2D;

public class ForceRegistration {
    public ForceGenerator fg;
    public Rigidbody2D rb;

    public ForceRegistration(ForceGenerator fg, Rigidbody2D rb) {
        this.fg = fg;
        this.rb = rb;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other.getClass() != ForceRegistration.class) return false;

        ForceRegistration fr = (ForceRegistration)other;
        return fr.rb == this.rb && fr.fg == this.fg;
    }
}

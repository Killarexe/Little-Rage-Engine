package net.killarexe.littlerage.engine.physics2d.components;

import net.killarexe.littlerage.engine.gameObject.components.Component;
import org.joml.Vector2f;

public class Collider extends Component {

    protected Vector2f offset = new Vector2f();

    public Vector2f getOffset() {
        return this.offset;
    }
}

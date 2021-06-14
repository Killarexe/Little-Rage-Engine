package net.killarexe.littlerage.engine.gameObject.components;

import net.killarexe.littlerage.engine.editor.LRImGui;
import org.joml.Vector2f;

public class Transform extends Component{

    public Vector2f pos;
    public Vector2f scale;
    public float rotation = 0f;
    public int zIndex;

    public Transform(){
        init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f pos){
        init(pos, new Vector2f());
    }

    public Transform(Vector2f pos, Vector2f scale){
        init(pos, scale);
    }

    public void init(Vector2f pos, Vector2f scale){
        this.pos = pos;
        this.scale = scale;
        this.zIndex = 0;
    }

    public Transform copy(){
        return new Transform(new Vector2f(this.pos), new Vector2f(this.scale));
    }

    public void copy(Transform transform){
        transform.pos.set(this.pos);
        transform.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if(!(o instanceof Transform)) return false;

        Transform transform = (Transform)o;
        return transform.pos.equals(this.pos)
                && transform.scale.equals(this.scale)
                && transform.rotation == this.rotation
                && transform.zIndex == this.zIndex;
    }
}

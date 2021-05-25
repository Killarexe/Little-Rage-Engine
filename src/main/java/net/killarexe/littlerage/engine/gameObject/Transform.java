package net.killarexe.littlerage.engine.gameObject;

import org.joml.Vector2f;

import java.util.Objects;

public class Transform {

    public Vector2f pos;
    public Vector2f scale;
    public float rotation = 0f;

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
        return transform.pos.equals(this.pos) && transform.scale.equals(this.scale);
    }
}

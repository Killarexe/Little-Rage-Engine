package net.killarexe.littlerage.engine.renderer;

import net.killarexe.littlerage.engine.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Sprite {

    private int width, height;

    private Texture texture = null;
    private Vector2f[] texCoords= {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

    public Texture getTexture(){
        return this.texture;
    }

    public Vector2f[] getTexCoords(){
        return texCoords;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTexID() {
        return texture == null ? -1 : texture.getTexID();
    }

    public void setTexture(Texture texture){
        this.texture = texture;
    }

    public void setTexCoords(Vector2f[] texCoords){
        this.texCoords = texCoords;
    }
}

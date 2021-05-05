package net.killarexe.littlerage.engine.gameObject;

import net.killarexe.littlerage.engine.renderer.Sprite;
import net.killarexe.littlerage.engine.gameObject.components.SpriteRenderer;
import org.joml.Vector2f;

public class Prefabs {

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY){
        GameObject block = new GameObject("Sprite_Prefab", new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponents(renderer);
        return block;
    }
}

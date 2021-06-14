package net.killarexe.littlerage.engine.gameObject;

import net.killarexe.littlerage.engine.Window;
import net.killarexe.littlerage.engine.gameObject.components.Transform;
import net.killarexe.littlerage.engine.renderer.Sprite;
import net.killarexe.littlerage.engine.gameObject.components.SpriteRenderer;
import org.joml.Vector2f;

public class Prefabs {

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY){
        GameObject block = Window.getScene().createGameObject("Sprite_Object");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponents(renderer);
        return block;
    }
}

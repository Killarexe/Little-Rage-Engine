package net.killarexe.littlerage.engine.scene;

import net.killarexe.littlerage.engine.Window;
import net.killarexe.littlerage.engine.gameObject.Camera;
import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.gameObject.Transform;
import net.killarexe.littlerage.engine.gameObject.components.Sprite;
import net.killarexe.littlerage.engine.gameObject.components.SpriteRenderer;
import net.killarexe.littlerage.engine.gameObject.components.SpriteSheet;
import net.killarexe.littlerage.engine.util.AssetPool;
import net.killarexe.littlerage.engine.util.Logger;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditiorScene extends Scene{

    private Logger logger = new Logger(getClass());

    private GameObject obj1;
    private SpriteSheet spriteSheet;

    public LevelEditiorScene(){
        logger.info("Changing to LevelEditior Scene");
    }

    @Override
    public void init() {
        loadResoucres();

        this.camera = new Camera(new Vector2f());

        spriteSheet = AssetPool.getSpriteSheet("src\\main\\resources\\assets\\textures\\tile\\Tile1.png");

        obj1 = new GameObject("Test 1",
                new Transform(new Vector2f(100,100), new Vector2f(256, 256)),
                1);
        obj1.addComponents(new SpriteRenderer(spriteSheet.getSprite(57)));
        this.addGameObjectToScene(obj1);
        GameObject obj2 = new GameObject("Test 2",
                new Transform(new Vector2f(200,100), new Vector2f(256, 256)),
                2);
        obj2.addComponents(new SpriteRenderer(spriteSheet.getSprite(56)));
        this.addGameObjectToScene(obj2);

    }

    private void loadResoucres(){
        AssetPool.getShader("src\\main\\resources\\assets\\shaders\\default.glsl");

        AssetPool.addSpriteSheet("src\\main\\resources\\assets\\textures\\tile\\Tile1.png",
                new SpriteSheet(AssetPool.getTexture("src\\main\\resources\\assets\\textures\\tile\\Tile1.png"),
                        16,
                        16,
                        64,
                        0));
    }

    @Override
    public void update(float dt) {
        logger.debug("FPS: " + (int) (1.0f / dt));

        for(GameObject go : this.gameObjects){
            go.update(dt);
        }

        this.renderer.render();
    }
}

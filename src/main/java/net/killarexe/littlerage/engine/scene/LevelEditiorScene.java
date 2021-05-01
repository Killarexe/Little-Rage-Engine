package net.killarexe.littlerage.engine.scene;

import net.killarexe.littlerage.engine.Window;
import net.killarexe.littlerage.engine.gameObject.Camera;
import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.gameObject.Transform;
import net.killarexe.littlerage.engine.gameObject.components.SpriteRenderer;
import net.killarexe.littlerage.engine.util.Logger;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditiorScene extends Scene{

    private Logger logger = new Logger(getClass());

    public LevelEditiorScene(){
        logger.info("Changing to LevelEditior Scene");
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(600 - xOffset * 2);
        float totalHeight = (float)(300 - yOffset * 2);
        float sizeX = totalWidth/100.0F;
        float sizeY = totalHeight/100.0F;

        for(int x=0; x < 100; x++){
            for(int y=0; y < 100; y++){
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                GameObject object = new GameObject("Obj" + x + ", " + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                object.addComponents(new SpriteRenderer(new Vector4f(xPos/totalWidth, yPos/totalHeight, 1, 1)));
                this.addGameObjectToScene(object);
            }
        }
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

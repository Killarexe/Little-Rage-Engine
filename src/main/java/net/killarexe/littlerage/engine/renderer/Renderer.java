package net.killarexe.littlerage.engine.renderer;

import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.gameObject.components.SpriteRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;
    private static Shader currentShader;

    public Renderer(){
        this.batches = new ArrayList<>();
    }

    public void add(GameObject object){
        SpriteRenderer renderer = object.getComponents(SpriteRenderer.class);
        if(renderer != null){
            add(renderer);
        }
    }

    private void add(SpriteRenderer renderer){
        boolean added = false;
        for(RenderBatch batch: batches){
            if(batch.hasRoom() && batch.getzIndex() == renderer.gameObject.transform.zIndex){
                Texture texture = renderer.getTexture();
                if(texture == null || batch.hasTexture(texture) || batch.hasTextureRoom()) {
                    batch.addSprite(renderer);
                    added = true;
                    break;
                }
            }
        }

        if(!added){
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, renderer.gameObject.transform.zIndex);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(renderer);
            Collections.sort(batches);
        }
    }

    public void render(){
        currentShader.use();
        for (int i = 0; i < batches.size(); i++) {
            RenderBatch batch = batches.get(i);
            batch.render();
        }
    }

    public void destroyGameObject(GameObject object){
        if(object.getComponents(SpriteRenderer.class) == null){return;}

        for (RenderBatch batch: batches) {
                if(batch.destroyIfExists(object)){
                return;
            }
        }
    }

    public static void bindShader(Shader shader){
        currentShader = shader;
    }

    public static Shader getBoundShader() {
        return currentShader;
    }
}

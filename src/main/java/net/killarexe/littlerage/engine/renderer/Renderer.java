package net.killarexe.littlerage.engine.renderer;

import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.gameObject.components.SpriteRenderer;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

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
            if(batch.hasRoom()){
                batch.addSprite(renderer);
                added = true;
                break;
            }
        }

        if(!added){
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(renderer);
        }
    }

    public void render(){
        for(RenderBatch batch: batches){
            batch.render();
        }
    }
}

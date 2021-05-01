package net.killarexe.littlerage.engine.scene;

import net.killarexe.littlerage.engine.gameObject.Camera;
import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.gameObject.Transform;
import net.killarexe.littlerage.engine.gameObject.components.SpriteRenderer;
import net.killarexe.littlerage.engine.renderer.Renderer;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene(){

    }

    public abstract void update(float dt);
    public void init(){

    }

    public void start(){
        for(GameObject go : gameObjects){
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject gameObject){
        if(!isRunning){
            gameObjects.add(gameObject);
        }else{
            gameObjects.add(gameObject);
            gameObject.start();
            this.renderer.add(gameObject);
        }
    }

    public Camera camera(){
        return this.camera;
    }

    public void render(){

    }
}

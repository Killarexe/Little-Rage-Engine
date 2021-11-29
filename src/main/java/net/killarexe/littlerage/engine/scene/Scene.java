package net.killarexe.littlerage.engine.scene;

import com.google.gson.*;
import net.killarexe.littlerage.engine.gameObject.*;
import net.killarexe.littlerage.engine.gameObject.components.*;
import net.killarexe.littlerage.engine.gson.*;
import net.killarexe.littlerage.engine.physics2d.Physics2D;
import net.killarexe.littlerage.engine.renderer.Renderer;
import net.killarexe.littlerage.engine.util.Logger;
import org.joml.Vector2f;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Scene {

    private Renderer renderer;
    private Camera camera;
    private boolean isRunning;
    private List<GameObject> gameObjects;
    private Logger logger = new Logger(getClass());
    private SceneInitializer initializer;
    private Physics2D physics2D;

    public Scene(SceneInitializer initializer){
        this.initializer = initializer;
        this.physics2D = new Physics2D();
        this.renderer = new Renderer();
        this.gameObjects = new ArrayList<>();
        this.isRunning = false;
    }

    public void editorUpdate(float dt){
        this.camera.adjustProjection();
        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).editorUpdate(dt);

            if(gameObjects.get(i).isDead()){
                gameObjects.remove(i);
                this.renderer.destroyGameObject(gameObjects.get(i));
                this.physics2D.destroyGameObject(gameObjects.get(i));
                i--;
            }
        }
    }

    public void update(float dt){
        this.camera.adjustProjection();
        this.physics2D.update(dt);
        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).update(dt);

            if(gameObjects.get(i).isDead()){
                gameObjects.remove(i);
                this.renderer.destroyGameObject(gameObjects.get(i));
                this.physics2D.destroyGameObject(gameObjects.get(i));
                i--;
            }
        }
    }

    public void init(){
        this.camera = new Camera(new Vector2f(-250, 0));
        this.initializer.loadResoucres(this);
        this.initializer.init(this);
    }

    public void start(){
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.start();
            this.renderer.add(go);
            this.physics2D.add(go);
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
            this.physics2D.add(gameObject);
        }
    }

    public Camera camera(){
        return this.camera;
    }

    public void render(){
        this.renderer.render();
    }

    public void imgui(){
        this.initializer.imgui();
    }

    public GameObject createGameObject(String name){
        GameObject gameObject = new GameObject(name);
        gameObject.addComponents(new Transform());
        gameObject.transform = gameObject.getComponents(Transform.class);
        return gameObject;
    }

    public void save(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserialiser())
                .create();

        String fileName = "data/level/level.lr";
        try {
            FileWriter writer = new FileWriter(fileName);
            List<GameObject> objectsToSer = new ArrayList();
            for(GameObject object: this.gameObjects){
                if(object.isDoSerialization()){
                    objectsToSer.add(object);
                }
            }

            writer.write(gson.toJson(objectsToSer));
            writer.close();
        }catch (IOException e){
            logger.error("Couldn't write the file: " + fileName);
        }
    }

    public void load(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserialiser())
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("data/level/level.lr")));
        }catch (IOException e){
            logger.warn("Couldn't find/load the file: " + inFile);
            logger.info("Creating level file...");
        }

        if(!inFile.equals("")){
            int maxGoId = 1;
            int maxCompId = -1;
            GameObject[] objects = gson.fromJson(inFile, GameObject[].class);
            for(int i=0; i < objects.length; i++){
                addGameObjectToScene(objects[i]);

                for(Component c: objects[i].getAllComponents()){
                    if(c.getUid() > maxCompId){
                        maxCompId = c.getUid();
                    }
                }

                if(objects[i].getUid() > maxCompId){
                    maxCompId = objects[i].getUid();
                }
            }

            maxGoId++;
            maxCompId++;
            logger.debug("" + maxGoId + " GameObjects. " + maxCompId + " Components");
            GameObject.init(maxGoId);
            Component.init(maxCompId);
        }
    }

    public void destroy(){
        for (GameObject object: gameObjects) {
            object.destroy();
        }
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public GameObject getGameObject(int gameObjectId){
        Optional<GameObject> result = this.gameObjects.stream().filter(gameObject -> gameObject.getUid() == gameObjectId).findFirst();
        return result.orElse(null);
    }
}

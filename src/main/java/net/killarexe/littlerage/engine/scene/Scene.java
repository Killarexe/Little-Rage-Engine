package net.killarexe.littlerage.engine.scene;

import com.google.gson.*;
import net.killarexe.littlerage.engine.gameObject.*;
import net.killarexe.littlerage.engine.gameObject.components.Component;
import net.killarexe.littlerage.engine.gson.*;
import net.killarexe.littlerage.engine.renderer.Renderer;
import net.killarexe.littlerage.engine.util.Logger;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected boolean loadedLevel = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected Logger logger = new Logger(getClass());

    public Scene(){

    }

    public abstract void update(float dt);
    public void init(){}

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

    public GameObject getGameObject(int gameObjectId){
        Optional<GameObject> result = this.gameObjects.stream().filter(gameObject -> gameObject.getUid() == gameObjectId).findFirst();
        return result.orElse(null);
    }

    public Camera camera(){
        return this.camera;
    }

    public void render(){}

    public void imgui(){}

    public void saveExit(){
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
            logger.error("Couldn't find/load the file: " + inFile);
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
            this.loadedLevel = true;
        }
    }
}

package net.killarexe.littlerage.engine.gameObject;

import imgui.ImGui;
import net.killarexe.littlerage.engine.gameObject.components.Component;
import net.killarexe.littlerage.engine.gameObject.components.Transform;
import net.killarexe.littlerage.engine.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int uid = -1;

    private String name;
    private List<Component> components;
    public Transform transform;

    private boolean doSerialization = true;
    private boolean isDead = false;

    transient Logger logger = new Logger(getClass());

    public GameObject(String name){
        this.name = name;
        this.components = new ArrayList<>();
        this.uid = ID_COUNTER++;
    }

    public <T extends Component> T getComponents(Class<T> componentClass){
        for(Component c : components){
            if(componentClass.isAssignableFrom(c.getClass())){
                try {
                    return componentClass.cast(c);
                }catch (ClassCastException e){
                    e.printStackTrace();
                    logger.error("Casting Component");
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponents(Class<T> componentClass){
        for(int i=0; i < components.size(); i++){
            Component c = components.get(i);
            if(componentClass.isAssignableFrom(c.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public void addComponents(Component c){
        c.generateId();
        this.components.add(c);
        c.gameObject = this;
    }

    public void update(float dt){
        for(int i=0; i < components.size(); i++){
            components.get(i).update(dt);
        }
    }
    public void editorUpdate(float dt){
        for(int i=0; i < components.size(); i++){
            components.get(i).editorUpdate(dt);
        }
    }

    public void start(){
        for(int i=0; i < components.size(); i++){
            components.get(i).start();
        }
    }

    public static void init(int maxId){
        ID_COUNTER = maxId;
    }

    public void destroy(){
        this.isDead = true;
        for (int i = 0; i < components.size(); i++) {
            components.get(i).destroy();
        }
    }

    public void imgui(){
        for(Component c: components){
            if(ImGui.collapsingHeader(c.getClass().getSimpleName())) {
                c.imgui();
            }
        }
    }

    public void setDoSerialization(boolean val){ this.doSerialization = val; }

    public List<Component> getAllComponents(){ return this.components; }
    public int getUid(){return this.uid;}
    public boolean isDoSerialization() { return this.doSerialization; }
    public boolean isDead() { return isDead; }
}

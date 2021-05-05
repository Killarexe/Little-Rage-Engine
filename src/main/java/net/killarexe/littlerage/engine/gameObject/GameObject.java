package net.killarexe.littlerage.engine.gameObject;

import net.killarexe.littlerage.engine.gameObject.components.Component;
import net.killarexe.littlerage.engine.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int uid = -1;

    private String name;
    private List<Component> components;
    public Transform transform;
    private int zIndex;

    transient Logger logger = new Logger(getClass());

    public GameObject(String name, Transform transform, int zIndex){
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
        this.zIndex = zIndex;
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

    public void start(){
        for(int i=0; i < components.size(); i++){
            components.get(i).start();
        }
    }

    public int getzIndex(){
        return this.zIndex;
    }
    
    public void imgui(){
        for(Component c: components){
            c.imgui();
        }
    }

    public List<Component> getAllComponents(){
        return this.components;
    }

    public static void init(int maxId){
        ID_COUNTER = maxId;
    }

    public int getUid(){return this.uid;}
}

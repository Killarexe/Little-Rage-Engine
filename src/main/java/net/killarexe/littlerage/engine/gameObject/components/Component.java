package net.killarexe.littlerage.engine.gameObject.components;

import imgui.ImGui;
import imgui.type.ImInt;
import net.killarexe.littlerage.engine.imGui.LRImGui;
import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.util.Logger;
import org.jbox2d.dynamics.BodyType;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {

    private static int ID_COUNTER = 0;
    private int uid = -1;

    public transient GameObject gameObject = null;
    protected transient Logger logger = new Logger(getClass());

    public void update(float dt){}
    public void editorUpdate(float dt){}
    public void start(){}
    public void imgui(){
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for(Field field: fields){
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());

                if(isTransient){
                    continue;
                }

                if(isPrivate){
                    field.setAccessible(true);
                }

                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if(type == int.class){
                    int val = (int)value;
                    field.set(this, LRImGui.dragInt(name, val));
                }else if(type == float.class){
                    float val = (float)value;
                    field.set(this, LRImGui.dragFloat(name, val));
                }else if(type == boolean.class){
                    boolean val = (boolean)value;
                    if(ImGui.checkbox(name + ": ", val)){
                        field.set(this, !val);
                    }
                }else if(type == Vector2f.class){
                    Vector2f val = (Vector2f)value;
                    LRImGui.drawVec2Control(name, val);
                }else if(type == Vector3f.class){
                    Vector3f val = (Vector3f)value;
                    LRImGui.drawVec3Control(name, val);
                }else if(type == Vector4f.class){
                    Vector4f val = (Vector4f)value;
                    float[] imVec = {val.x , val.y, val.z, val.w};
                    if(ImGui.dragFloat4(name + ": ", imVec)){
                        val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                    }
                }else if(type.isEnum()){
                    String[] vals = getEnumValues(type);
                    String enumType = ((Enum)value).name();
                    ImInt index = new ImInt(indexOf(enumType, vals));
                    if(ImGui.combo(field.getName(), index, vals, vals.length)){
                        field.set(this, type.getEnumConstants()[index.get()]);
                    }
                }

                if(isPrivate){
                    field.setAccessible(false);
                }
            }
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    public void generateId(){
        if(this.uid == -1){
            this.uid = ID_COUNTER++;
        }
    }

    public void destroy(){

    }

    public static void init(int maxId){
        ID_COUNTER = maxId;
    }

    public int getUid(){return this.uid;}

    private <T extends Enum<T>> String[] getEnumValues(Class<T> enumType) {
        String[] enumValues = new String[enumType.getEnumConstants().length];
        int i = 0;
        for (T enumIntegerValue : enumType.getEnumConstants()) {
            enumValues[i] = enumIntegerValue.name();
            i++;
        }
        return enumValues;
    }

    private int indexOf(String str, String[] arr) {
        for (int i=0; i < arr.length; i++) {
            if (str.equals(arr[i])) {
                return i;
            }
        }

        return -1;
    }
}

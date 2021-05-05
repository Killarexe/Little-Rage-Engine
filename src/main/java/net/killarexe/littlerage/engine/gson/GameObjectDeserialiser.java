package net.killarexe.littlerage.engine.gson;

import com.google.gson.*;
import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.gameObject.Transform;
import net.killarexe.littlerage.engine.gameObject.components.Component;

import java.lang.reflect.Type;

public class GameObjectDeserialiser implements JsonDeserializer<GameObject> {

    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("components");
        Transform transform = context.deserialize(jsonObject.get("transform"), Transform.class);
        int zIndex = context.deserialize(jsonObject.get("zIndex"), int.class);

        GameObject gameObject = new GameObject(name, transform, zIndex);
        for(JsonElement e: components){
            Component c = context.deserialize(e, Component.class);
            gameObject.addComponents(c);
        }
        return gameObject;
    }
}

package net.killarexe.littlerage.engine.util;

import net.killarexe.littlerage.engine.renderer.SpriteSheet;
import net.killarexe.littlerage.engine.renderer.Shader;
import net.killarexe.littlerage.engine.renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();

    private static Logger logger = new Logger(AssetPool.class);

    public static Shader getShader(String resourceName){
        File file = new File(resourceName);
        if(AssetPool.shaders.containsKey(file.getAbsolutePath())){
            return AssetPool.shaders.get(file.getAbsolutePath());
        }else{
            Shader shader = new Shader(resourceName);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName){
        File file = new File(resourceName);
        if(AssetPool.textures.containsKey(file.getAbsolutePath())){
            return AssetPool.textures.get(file.getAbsolutePath());
        }else{
            Texture texture = new Texture();
            texture.init(resourceName);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpriteSheet(String resourceName, SpriteSheet spriteSheet){
        File file = new File(resourceName);
        if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())){
            AssetPool.spriteSheets.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static SpriteSheet getSpriteSheet(String resourceName){
        File file = new File(resourceName);
        if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())){
            logger.fatal("Failed to tried to access spritesheet '" + resourceName + "'");
        }
        return AssetPool.spriteSheets.getOrDefault(file.getAbsolutePath(), null);
    }
}

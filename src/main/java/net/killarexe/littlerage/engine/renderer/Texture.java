package net.killarexe.littlerage.engine.renderer;

import net.killarexe.littlerage.engine.util.Logger;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

    private String filePath;
    private int texID;
    private int width, height;

    Logger logger = new Logger(getClass());

    //public Texture(String filePath){
    //}

    public void init(String filePath){
        this.filePath = filePath;
        logger.info("Loading Texture: " + filePath);

        //Generate Texture on GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        //Set texture parameters
        //Repeat the image
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        //When stretching an image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        //When shrinking an image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(filePath, width, height, channels, 0);

        if(image != null){
            this.width = width.get(0);
            this.height = height.get(0);

            if(channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            }else if(channels.get(0) == 4){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            }else{
                logger.fatal("Failed to load image '" + filePath + "' unknown number of channels: " + channels.get(0));
            }
        }else{
            logger.fatal("Couldn't load the image '" + filePath + "'");
        }

        stbi_image_free(image);
    }
    public void bind(){glBindTexture(GL_TEXTURE_2D, texID);}
    public void unBind(){glBindTexture(GL_TEXTURE_2D, 0);}

    public int getWidth(){return this.width;}
    public int getHeight(){return this.height;}
    public int getTexID(){return this.texID;};
}

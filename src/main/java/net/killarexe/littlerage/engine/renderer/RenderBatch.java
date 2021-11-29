package net.killarexe.littlerage.engine.renderer;

import net.killarexe.littlerage.engine.Window;
import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.gameObject.components.SpriteRenderer;
import net.killarexe.littlerage.engine.util.AssetPool;
import net.killarexe.littlerage.engine.util.Logger;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.ARBVertexArrayObject.*;

public class RenderBatch implements Comparable<RenderBatch>{
    //Vertex
    //======
    // pos                  Color                           TexCoords       TexId   EntityId
    // float, float,        float, float, float, float      float, float    float   float

    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;
    private final int ENTITY_ID_SIZE = 1;
    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
    private final int ENITY_ID_OFFSET = TEX_ID_OFFSET + TEX_ID_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 10;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    private List<Texture> textures;
    private int vaoID, vboID;
    private int maxBatchSize;
    private int zIndex;

    Logger logger = new Logger(getClass());

    public RenderBatch(int maxBatchSize, int zImdex){
        this.zIndex = zImdex;
        logger.info("Creating RenderBatch");
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        // 4 vertices quads
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numSprites = 0;
        this.hasRoom = true;
        this.textures = new ArrayList<>();
    }

    public void start(){
        //Generate and bind a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Allocate space for vertics
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //Create and Upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = genIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        //Enable the buffer and attibute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);

        glVertexAttribPointer(4, ENTITY_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, ENITY_ID_OFFSET);
        glEnableVertexAttribArray(4);
    }

    public void addSprite(SpriteRenderer renderer){
        //Get the index and add rendererObject
        int index = this.numSprites;
        this.sprites[index] = renderer;
        this.numSprites++;

        if(renderer.getTexture() != null){
            if(!textures.contains(renderer.getTexture())){
                textures.add(renderer.getTexture());
            }
        }

        //add the properites to local vertices array
        loadVertexProperites(index);

        if(numSprites >= this.maxBatchSize){
            this.hasRoom = false;
        }
    }

    public void render(){
        boolean rebufferData = false;
        for(int i=0; i < numSprites; i++){
            SpriteRenderer spr = sprites[i];
            if(spr.isDirty()){
                loadVertexProperites(i);
                spr.setDirty(false);
                rebufferData = true;
            }
        }

        if(rebufferData) {
            //rebuffer all data every frame
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        //Use shader
        Shader shader = Renderer.getBoundShader();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());

        for(int i=0; i < textures.size(); i++){
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", texSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for(int i=0; i < textures.size(); i++){
            textures.get(i).unBind();
        }

        shader.detach();
    }

    private void loadVertexProperites(int index){
        SpriteRenderer spriteRenderer = this.sprites[index];

        //Find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = spriteRenderer.getColor();
        Vector2f[] texCoords = spriteRenderer.getTexCoords();

        int texId = 0;
        if(spriteRenderer.getTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if(textures.get(i).equals(spriteRenderer.getTexture())){
                    texId = i + 1;
                    break;
                }
            }
        }

        boolean isRotated = spriteRenderer.gameObject.transform.rotation != 0f;
        Matrix4f transformMatrix = new Matrix4f().identity();
        if(isRotated){
            transformMatrix.translate(spriteRenderer.gameObject.transform.pos.x, spriteRenderer.gameObject.transform.pos.y, 0f);
            transformMatrix.rotate((float)Math.toRadians(spriteRenderer.gameObject.transform.rotation), 0, 0 ,1);
            transformMatrix.scale(spriteRenderer.gameObject.transform.scale.x, spriteRenderer.gameObject.transform.scale.y, 1);
        }

        //Add vertices with the appropriate properties
        float xAdd = 0.5f;
        float yAdd = 0.5f;

        for(int i=0; i < 4; i++){
            if(i == 1){
                yAdd = -0.5f;
            }else if(i == 2){
                xAdd = -0.5f;
            }else if(i == 3){
                yAdd = 0.5f;
            }

            Vector4f currentPos = new Vector4f(spriteRenderer.gameObject.transform.pos.x + (xAdd * spriteRenderer.gameObject.transform.scale.x),
                    spriteRenderer.gameObject.transform.pos.y + (yAdd * spriteRenderer.gameObject.transform.scale.y), 0, 1);

            if(isRotated){
                currentPos = new Vector4f(xAdd, yAdd, 0, 1).mul(transformMatrix);
            }

            //Load pos
            vertices[offset] = currentPos.x;
            vertices[offset + 1] = currentPos.y;

            //load color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            //load texture coord
            vertices[offset + 6] = texCoords[i].x;
            vertices[offset + 7] = texCoords[i].y;

            //load texture id
            vertices[offset + 8] = texId;

            //load entity id
            vertices[offset + 9] = spriteRenderer.gameObject.getUid() + 1;

            offset += VERTEX_SIZE;
        }
    }

    private int[] genIndices(){
        //6 indices per quad(3 per triangle)
        int[] elements = new int[6 * maxBatchSize];
        for(int i=0; i < maxBatchSize; i++){
            loadElementsIndices(elements, i);
        }

        return elements;
    }

    private void loadElementsIndices(int[] elements, int index){
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        //Trangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;

        //Triangle 2
        elements[offsetArrayIndex + 3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public boolean destroyIfExists(GameObject object){
        SpriteRenderer renderer = object.getComponents(SpriteRenderer.class);

        for (int i = 0; i < numSprites; i++) {
            if(sprites[i] == renderer){
                for (int j = 0; j < numSprites - 1; j++) {
                    sprites[j] = sprites[j + 1];
                    sprites[j].setDirty(true);
                }
                numSprites--;
                return true;
            }
        }

        return false;
    }

    public boolean hasRoom(){
        return this.hasRoom;
    }
    public boolean hasTextureRoom(){
        return this.textures.size() < 8;
    }
    public boolean hasTexture(Texture texture){
        return this.textures.contains(texture);
    }
    public int getzIndex(){return this.zIndex;}

    @Override
    public int compareTo(RenderBatch o) {
        return Integer.compare(this.zIndex, o.zIndex);
    }
}

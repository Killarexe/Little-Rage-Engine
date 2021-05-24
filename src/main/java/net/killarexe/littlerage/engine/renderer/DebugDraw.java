package net.killarexe.littlerage.engine.renderer;

import net.killarexe.littlerage.engine.Window;
import net.killarexe.littlerage.engine.util.AssetPool;
import net.killarexe.littlerage.engine.util.JMath;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.killarexe.littlerage.engine.util.JMath.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class DebugDraw {
    private static int MAX_LINES = 500;
    private static List<Line2D> lines = new ArrayList<>();

    //6 floats per vertex, 2 vertices per line
    private static float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static Shader shader = AssetPool.getShader("assets/shaders/debugLine2D.glsl");

    private static int vboID, vaoID;

    private static boolean started = false;

    public static void start(){
        //Generate the vao
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Create the vbo and buffer some memory
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER ,vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //Enable the vertex array attributes
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glLineWidth(2f);
    }

    public static void beginFrame(){
        if(!started){
            start();
            started = true;
        }

        //Remove dead lines
        for(int i=0; i < lines.size(); i++){
            if(lines.get(i).beginFrame() < 0){
                lines.remove(i);
                i--;
            }
        }
    }

    public static void draw(){
        if(lines.size() <= 0)return;

        int index = 0;
        for(Line2D line: lines){
            for(int i=0; i < 2; i++){
                Vector2f pos = i == 0 ? line.getFrom() : line.getTo();
                Vector3f color = line.getColor();

                //Load pos
                vertexArray[index] = pos.x;
                vertexArray[index + 1] = pos.y;
                vertexArray[index + 2] = -10.0f;

                //Load color
                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;
                index += 6;
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2));

        //Use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());

        //Bind vao
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        //Draw the batch
        glDrawArrays(GL_LINES, 0, lines.size() * 6 * 2);

        //Disable Location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.detach();
    }

    //Add line2D methods
    public static void addLine2D(Vector2f from, Vector2f to){
        addLine2D(from, to, new Vector3f(0,1,0), 1);
    }

    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color){
        addLine2D(from, to, color, 1);
    }

    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifeTime){
        if(lines.size() >= MAX_LINES) return;
        DebugDraw.lines.add(new Line2D(from, to, color, lifeTime));
    }

    //Add Box2D methods
    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation){
        addBox2D(center, dimensions, rotation, new Vector3f(1,1,1), 1);
    }

    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation, Vector3f color){
        addBox2D(center, dimensions, rotation, color, 1);
    }

    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation, Vector3f color, int lifeTime){
        Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).mul(0.5f));
        Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).mul(0.5f));

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
                new Vector2f(max.x, max.y), new Vector2f(max.x, min.y)
        };

        if(rotation != 0.0f){
            for(Vector2f vert: vertices){
                rotate(vert, rotation, center);
            }
        }

        addLine2D(vertices[0], vertices[1], color, lifeTime);
        addLine2D(vertices[0], vertices[3], color, lifeTime);
        addLine2D(vertices[1], vertices[2], color, lifeTime);
        addLine2D(vertices[2], vertices[3], color, lifeTime);
    }

    //Add Circle methods
    public static void addLine2D(Vector2f center, float radius){
        addCircle(center, radius, new Vector3f(0,1,0), 1);
    }

    public static void addLine2D(Vector2f center, float radius, Vector3f color){
        addCircle(center, radius, color, 1);
    }

    public static void addCircle(Vector2f center, float radius, Vector3f color, int lifeTime){
        Vector2f[] points = new Vector2f[20];
        int increment = 360 / points.length;
        int currentAngle = 0;

        for(int i=0; i < points.length; i++){
            Vector2f tmp = new Vector2f(radius, 0);
            JMath.rotate(tmp, currentAngle, new Vector2f());
            points[i] = new Vector2f(tmp).add(center);

            if(i > 0){
                addLine2D(points[i - 1], points[i], color, lifeTime);
            }
            currentAngle += increment;
        }
        addLine2D(points[points.length - 1], points[0], color, lifeTime);
    }
}

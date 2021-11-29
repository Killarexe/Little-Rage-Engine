package net.killarexe.littlerage.engine.gameObject;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix, inverseProjection, inverseView;
    public Vector2f pos;
    private float projectionWidth = 6f;
    private float projectionHeight = 3f;
    private Vector2f projectionSize = new Vector2f(projectionWidth, projectionHeight);
    private float zoom = 1.0f;

    public Camera(Vector2f pos){
        this.pos = pos;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection(){
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, projectionSize.x * this.zoom, 0.0f, projectionSize.y * this.zoom, 0.0f, 100.0f);
        projectionMatrix.invert(inverseProjection);
    }

    public Matrix4f getViewMatrix(){
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        this.viewMatrix = viewMatrix.lookAt(
                new Vector3f(pos.x, pos.y, 20.0f),
                cameraFront.add(pos.x, pos.y, 0.0f),
                cameraUp);
        this.viewMatrix.invert(inverseView);
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix(){
        return this.projectionMatrix;
    }
    public Matrix4f getInverseProjection() {return inverseProjection;}
    public Matrix4f getInverseView() {return inverseView;}
    public Vector2f getProjectionSize(){return this.projectionSize;}
    public float getZoom(){return zoom;}

    public void setZoom(float newZoom){this.zoom = newZoom;}

    public void addZoom(float val){this.zoom += val;}
}

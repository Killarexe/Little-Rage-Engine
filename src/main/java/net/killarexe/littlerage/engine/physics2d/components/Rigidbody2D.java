package net.killarexe.littlerage.engine.physics2d.components;

import net.killarexe.littlerage.engine.gameObject.components.Component;
import net.killarexe.littlerage.engine.physics2d.enums.BodyType;
import org.jbox2d.dynamics.Body;
import org.joml.Vector2f;

public class Rigidbody2D extends Component {

    private boolean fixedRotation = false;
    private boolean continousCollision = true;
    private float angularDamping = 0.8f;
    private float linearDamping = 0.9f;
    private float mass = 0;

    private Vector2f vel = new Vector2f();
    private BodyType bodyType = BodyType.Dynamic;
    private transient Body rawBody = null;

    @Override
    public void update(float dt) {
        if(rawBody != null){
            this.gameObject.transform.pos.set(rawBody.getPosition().x, rawBody.getPosition().y);
            this.gameObject.transform.rotation = (float)Math.toDegrees(rawBody.getAngle());
        }
    }

    public Vector2f getVel() {
        return vel;
    }

    public void setVel(Vector2f vel) {
        this.vel = vel;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public Body getRawBody() {
        return rawBody;
    }

    public void setRawBody(Body rawBody) {
        this.rawBody = rawBody;
    }

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }

    public boolean isContinousCollision() {
        return continousCollision;
    }

    public void setContinousCollision(boolean continousCollision) {
        this.continousCollision = continousCollision;
    }

    public float getAngularDamping() {
        return angularDamping;
    }

    public void setAngularDamping(float angularDamping) {
        this.angularDamping = angularDamping;
    }

    public float getLinearDamping() {
        return linearDamping;
    }

    public void setLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }
}

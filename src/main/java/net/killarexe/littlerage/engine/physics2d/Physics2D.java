package net.killarexe.littlerage.engine.physics2d;

import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.gameObject.components.Transform;
import net.killarexe.littlerage.engine.physics2d.components.*;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.joml.Vector2f;

public class Physics2D {

    private Vec2 gravity = new Vec2(0, -10f);
    private World world = new World(gravity);

    private float physicsTime = 0f;
    private float physicsTimeStep = 1f / 60f;
    private int velIterations = 8;
    private int posIterations = 3;

    public void add(GameObject object){
        Rigidbody2D rigidbody2D = object.getComponents(Rigidbody2D.class);
        if(rigidbody2D != null && rigidbody2D.getRawBody() == null){
            Transform transform = object.transform;

            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = (float)Math.toRadians(transform.rotation);
            bodyDef.position.set(transform.pos.x, transform.pos.y);
            bodyDef.angularDamping = rigidbody2D.getAngularDamping();
            bodyDef.linearDamping = rigidbody2D.getLinearDamping();
            bodyDef.fixedRotation = rigidbody2D.isFixedRotation();
            bodyDef.bullet = rigidbody2D.isContinousCollision();

            switch (rigidbody2D.getBodyType()){
                case Kinematic:
                    bodyDef.type = BodyType.KINEMATIC;
                    break;
                case Static:
                    bodyDef.type = BodyType.STATIC;
                    break;
                case Dynamic:
                    bodyDef.type = BodyType.DYNAMIC;
                    break;
            }

            PolygonShape shape = new PolygonShape();
            CircleCollider circleCollider;
            Box2DCollider box2DCollider;

            if((circleCollider = object.getComponents(CircleCollider.class)) != null){
                shape.setRadius(circleCollider.getRadius());
            }else if((box2DCollider = object.getComponents(Box2DCollider.class)) != null){
                Vector2f halfSize = new Vector2f(box2DCollider.getHalfSize()).mul(0.5f);
                Vector2f offset = box2DCollider.getOffset();
                Vector2f origin = new Vector2f(box2DCollider.getOrigin());
                shape.setAsBox(halfSize.x, halfSize.y, new Vec2(origin.x, origin.y), 0);

                Vec2 pos = bodyDef.position;
                float xPos = pos.x + offset.x;
                float yPos = pos.y + offset.y;
                bodyDef.position.set(xPos, yPos);
            }

            Body body = this.world.createBody(bodyDef);
            rigidbody2D.setRawBody(body);
            body.createFixture(shape, rigidbody2D.getMass());
        }
    }

    public void update(float dt){
        physicsTime += dt;
        if(physicsTime >= 0f){
            physicsTime -= physicsTimeStep;
            world.step(physicsTimeStep, velIterations, posIterations);
        }
    }

    public void destroyGameObject(GameObject object){
        Rigidbody2D rigidbody2D = object.getComponents(Rigidbody2D.class);
        if(rigidbody2D != null && rigidbody2D.getRawBody() != null){
            world.destroyBody(rigidbody2D.getRawBody());
            rigidbody2D.setRawBody(null);
        }
    }
}
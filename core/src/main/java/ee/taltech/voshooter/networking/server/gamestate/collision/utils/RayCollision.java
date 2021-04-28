package ee.taltech.voshooter.networking.server.gamestate.collision.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class RayCollision {

    private final Body collidedBody;
    private final Vector2 collisionPosition;


    public RayCollision(Body collidedBody, Vector2 collisionPosition) {
        this.collidedBody = collidedBody;
        this.collisionPosition = collisionPosition;
    }

    public Body getCollidedBody() {
        return collidedBody;
    }

    public Vector2 getCollisionPosition() {
        return collisionPosition;
    }
}

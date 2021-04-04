package ee.taltech.voshooter.entity;

import com.badlogic.gdx.math.Vector2;

public class Entity {

    protected Vector2 position;
    protected Vector2 velocity;

    /**
     * Construct this entity.
     * @param position The initial position of this entity.
     */
    public Entity(Vector2 position) {
        this.position = position;
    }

    public Entity(Vector2 position, Vector2 velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    /**
     * @return The position object tied to this entity.
     */
    public Vector2 getPosition() {
        return position.cpy();
    }

    public Vector2 getVelocity() {
        return velocity.cpy();
    }
}

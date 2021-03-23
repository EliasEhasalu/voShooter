package ee.taltech.voshooter.entity;

import com.badlogic.gdx.math.Vector2;

public class Entity {

    protected Vector2 position;

    /**
     * Construct this entity.
     * @param position The initial position of this entity.
     */
    public Entity(Vector2 position) {
        this.position = position;
    }

    /**
     * @return The position object tied to this entity.
     */
    public Vector2 getPosition() {
        return position;
    }
}

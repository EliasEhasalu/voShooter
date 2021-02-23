package ee.taltech.voshooter.entity;

import ee.taltech.voshooter.geometry.Pos;

public class Entity {

    protected Pos position;

    /**
     * Construct this entity.
     * @param position The initial position of this entity.
     */
    public Entity(Pos position) {
        this.position = position;
    }

    /**
     * @return The position object tied to this entity.
     */
    public Pos getPosition() {
        return position;
    }
}

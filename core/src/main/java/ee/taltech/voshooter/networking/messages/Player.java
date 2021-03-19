package ee.taltech.voshooter.networking.messages;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.geometry.Movable;
import ee.taltech.voshooter.geometry.Pos;

public class Player {

    public Pos pos;
    public Pos prevPos;
    public Vector2 vel;
    public long id;
    public String name;

    public Player() {
    }

    /**
     * @param initialPos The starting position of the player.
     */
    public Player(Pos initialPos, long id, String name) {
        this.pos = initialPos;
        this.vel = new Vector2(0f, 0f);
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return String.format("(%f, %f)", this.pos.getX(), this.pos.getY());
    }
}

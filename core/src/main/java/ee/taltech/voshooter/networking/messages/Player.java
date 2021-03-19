package ee.taltech.voshooter.networking.messages;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.geometry.Movable;
import ee.taltech.voshooter.geometry.Pos;

public class Player implements Movable {

    public Pos pos;
    public Vector2 vel;

    public Player() {
    }

    /**
     * @param initialPos The starting position of the player.
     */
    public Player(Pos initialPos) {
        this.pos = initialPos;
        this.vel = new Vector2(0f, 0f);
    }

    /**
     * Move the player.
     * @param newPos The position to move the player to.
     */
    public void setPos(Pos newPos) {
        this.pos = newPos;
    }

    public void setVel(Vector2 newVel) {
        this.vel = newVel;
    }

    public String toString() {
        return String.format("(%f, %f)", this.pos.getX(), this.pos.getY());
    }
}

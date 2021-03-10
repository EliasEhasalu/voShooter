package ee.taltech.voshooter.networking.messages;

import ee.taltech.voshooter.geometry.Movable;
import ee.taltech.voshooter.geometry.Pos;

public class Player implements Movable {

    Pos pos;

    /**
     * @param initialPos The starting position of the player.
     */
    public Player(Pos initialPos) {
        this.pos = initialPos;
    }

    /**
     * Move the player.
     * @param newPos The position to move the player to.
     */
    @Override
    public boolean setPos(Pos newPos) {
        this.pos = newPos;
        return true;
    }
}

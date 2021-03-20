package ee.taltech.voshooter.networking.messages.serverreceived;

import java.util.Objects;

public class MovePlayer extends PlayerAction {

    public int xDir;
    public int yDir;

    /** Serialize **/
    public MovePlayer() {
    }

    /**
     * @param xDir The direction to move the player on the x-axis.
     * @param yDir The direction to move the player on the y-axis.
     */
    public MovePlayer(int xDir, int yDir) {
        this.xDir = xDir;
        this.yDir = yDir;
    }


    @Override
    public final boolean equals(Object o) {
        return (
                getClass() == o.getClass()
                && xDir == ((MovePlayer) o).xDir
                && yDir == ((MovePlayer) o).yDir
        );
    }

    /**
     * Make sure messages with the same content get overridden in the
     * set of player messages awaiting processing.
     * @return The hashcode for this message.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), xDir, yDir);
    }
}

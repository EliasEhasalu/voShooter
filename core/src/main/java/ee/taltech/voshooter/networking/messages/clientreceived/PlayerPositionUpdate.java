package ee.taltech.voshooter.networking.messages.clientreceived;

import ee.taltech.voshooter.geometry.Pos;
import ee.taltech.voshooter.networking.messages.Player;

import java.util.List;

public class PlayerPositionUpdate {

    public long id;
    public Pos pos;

    public PlayerPositionUpdate() {
    }

    /**
     * @param id The id of the player.
     * @param pos The new position of the player.
     */
    public PlayerPositionUpdate(Pos pos, long id) {
        this.pos = pos;
        this.id = id;
    }
}

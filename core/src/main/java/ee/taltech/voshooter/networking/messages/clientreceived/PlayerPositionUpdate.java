package ee.taltech.voshooter.networking.messages.clientreceived;

import com.badlogic.gdx.math.Vector2;

public class PlayerPositionUpdate {

    public long id;
    public Vector2 pos;

    public PlayerPositionUpdate() {
    }

    /**
     * @param id The id of the player.
     * @param pos The new position of the player.
     */
    public PlayerPositionUpdate(Vector2 pos, long id) {
        this.pos = pos;
        this.id = id;
    }
}

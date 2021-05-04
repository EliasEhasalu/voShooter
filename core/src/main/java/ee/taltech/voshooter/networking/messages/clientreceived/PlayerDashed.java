package ee.taltech.voshooter.networking.messages.clientreceived;

import com.badlogic.gdx.math.Vector2;

public class PlayerDashed {

    public long id;
    public Vector2 direction;

    /** Serialization. */
    public PlayerDashed() {
    }

    public PlayerDashed(long id, Vector2 direction) {
        this.id = id;
        this.direction = direction;
    }
}

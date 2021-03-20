package ee.taltech.voshooter.networking.messages.clientreceived;

import com.badlogic.gdx.math.Vector2;

public class PlayerViewUpdate {

    public long id;
    public Vector2 viewDirection;

    /** Serialize. **/
    public PlayerViewUpdate() {
    }

    /**
     * @param id The id of the player.
     * @param viewDirection The new view direction of the player.
     */
    public PlayerViewUpdate(Vector2 viewDirection, long id) {
        this.viewDirection = viewDirection;
        this.id = id;
    }
}

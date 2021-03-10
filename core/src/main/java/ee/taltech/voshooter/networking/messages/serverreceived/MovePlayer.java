package ee.taltech.voshooter.networking.messages.serverreceived;

import java.util.List;

public class MovePlayer {

    public List<Integer> keyPresses;

    /** Serialize. */
    public MovePlayer() {
    }

    /**
     * Construct the message.
     * @param keyPresses A list of keypresses performed by the player.
     */
    public MovePlayer(List<Integer> keyPresses) {
        this.keyPresses = keyPresses;
    }
}

package ee.taltech.voshooter.networking.messages.clientreceived;

import ee.taltech.voshooter.networking.messages.Player;

import java.util.List;

public class PlayerPositionUpdate {

    public List<Player> players;

    public PlayerPositionUpdate() {
    }

    /**
     * @param players An update of players' state.
     */
    public PlayerPositionUpdate(List<Player> players) {
        this.players = players;
    }
}

package ee.taltech.voshooter.networking.messages.clientreceived;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;

public class PlayerKothChange {

    public Player player;

    /** Serialization. */
    public PlayerKothChange() {
    }

    public PlayerKothChange(Player player) {
        this.player = player;
    }
}

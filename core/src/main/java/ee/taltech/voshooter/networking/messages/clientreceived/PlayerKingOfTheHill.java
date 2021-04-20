package ee.taltech.voshooter.networking.messages.clientreceived;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;

public class PlayerKingOfTheHill {

    public Player player;

    /** Serialization. */
    public PlayerKingOfTheHill() {
    }

    public PlayerKingOfTheHill(Player player) {
        this.player = player;
    }
}

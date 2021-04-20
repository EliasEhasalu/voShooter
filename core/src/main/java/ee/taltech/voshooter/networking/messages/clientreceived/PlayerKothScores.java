package ee.taltech.voshooter.networking.messages.clientreceived;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;

import java.util.Map;

public class PlayerKothScores {

    public Map<Player, Double> players;

    public PlayerKothScores() {
    }

    public PlayerKothScores(Map<Player, Double> players) {
        this.players = players;
    }
}

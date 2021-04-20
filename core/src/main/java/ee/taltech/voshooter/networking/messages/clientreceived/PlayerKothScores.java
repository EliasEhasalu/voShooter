package ee.taltech.voshooter.networking.messages.clientreceived;

import java.util.Map;

public class PlayerKothScores {

    public Map<Long, Double> players;

    public PlayerKothScores() {
    }

    public PlayerKothScores(Map<Long, Double> players) {
        this.players = players;
    }
}

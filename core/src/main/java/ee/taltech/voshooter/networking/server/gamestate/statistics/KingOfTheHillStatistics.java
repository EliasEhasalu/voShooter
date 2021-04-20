package ee.taltech.voshooter.networking.server.gamestate.statistics;

import ee.taltech.voshooter.networking.messages.clientreceived.PlayerKothScores;
import ee.taltech.voshooter.networking.server.VoConnection;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;

import java.util.HashMap;
import java.util.Map;

public class KingOfTheHillStatistics {

    private int updateTicker = 0;
    private static final int FREQUENCY = 60;
    private Game parent;
    public Player playerInArea;
    private Map<Player, Double> kothPlayers = new HashMap<>();

    public KingOfTheHillStatistics(Game parent) {
        this.parent = parent;
    }

    public void sendUpdates() {
        updateTicker++;
        refreshPlayers();
        updatePlayerScoreKoth();
        sendPlayerScoreKoth();
    }

    public void refreshPlayers() {
        Map<Player, Double> newMap = new HashMap<>();
        for (Player player : parent.getPlayers()) {
            newMap.put(player, kothPlayers.getOrDefault(player, 0.0));
        }
        kothPlayers = newMap;
    }

    public void updatePlayerScoreKoth() {
        if (playerInArea != null) {
            kothPlayers.put(playerInArea, kothPlayers.get(playerInArea) + 1 / (double) FREQUENCY);
        }
    }

    public void sendPlayerScoreKoth() {
        if (updateTicker % (FREQUENCY / 3) == 0) {
            for (VoConnection c : parent.getConnections()) {
                c.sendTCP(new PlayerKothScores(kothPlayers));
            }
        }
    }
}

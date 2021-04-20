package ee.taltech.voshooter.networking.server.gamestate.gamemodes;

import ee.taltech.voshooter.networking.messages.clientreceived.PlayerKingOfTheHill;
import ee.taltech.voshooter.networking.server.VoConnection;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.LevelGenerator;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.server.gamestate.statistics.StatisticsTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KingOfTheHillManager extends GameMode {

    private final Game parent;
    public Map<Integer, Float> location;

    public KingOfTheHillManager(Game parent, StatisticsTracker statisticsTracker) {
        this.parent = parent;
        this.statisticsTracker = statisticsTracker;
        this.location = LevelGenerator.getKothLocation(parent.getCurrentMap());
    }

    @Override
    public void update() {
        checkIfAnyoneInArea();
        statisticsUpdates();
    }

    @Override
    public void statisticsUpdates() {
        statisticsTracker.sendUpdates();
    }

    private void checkIfAnyoneInArea() {
        List<Player> playersInArea = new ArrayList<>();
        for (Player player : parent.getPlayers()) {
            if (player.getPos().x > location.get(0) && player.getPos().x < location.get(1)
                    && player.getPos().y > location.get(2) && player.getPos().y < location.get(3)
                    && player.isAlive()) {
                playersInArea.add(player);
            }
        }
        if (playersInArea.size() == 1) {
            for (VoConnection c : parent.getConnections()) {
                c.sendTCP(new PlayerKingOfTheHill(playersInArea.get(0)));
            }
        }
    }
}

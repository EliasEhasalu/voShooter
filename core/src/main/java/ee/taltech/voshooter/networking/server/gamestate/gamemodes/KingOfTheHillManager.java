package ee.taltech.voshooter.networking.server.gamestate.gamemodes;

import ee.taltech.voshooter.networking.messages.clientreceived.PlayerKothChange;
import ee.taltech.voshooter.networking.server.VoConnection;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.LevelGenerator;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.server.gamestate.statistics.KingOfTheHillStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

public class KingOfTheHillManager extends GameMode {

    private final Game parent;
    private final KingOfTheHillStatistics statisticsTracker;
    public Map<Integer, Float> location;

    public KingOfTheHillManager(Game parent, KingOfTheHillStatistics statisticsTracker) {
        this.parent = parent;
        this.statisticsTracker = statisticsTracker;
        this.location = LevelGenerator.getKothLocation(parent.getCurrentMap());
    }

    @Override
    public void update() {
        calculateTimeLeft();
        sendKothAreaUpdates();
        statisticsUpdates();
    }

    @Override
    public void calculateTimeLeft() {
        OptionalDouble time = statisticsTracker.getHighestTimeHeld();
        double highestTime = 0;
        if (time.isPresent()) highestTime = time.getAsDouble();
        if (highestTime >= parent.gameLength) {
            parent.shutDown();
        }
    }

    @Override
    public void statisticsUpdates() {
        statisticsTracker.sendUpdates();
    }

    private void sendKothAreaUpdates() {
        List<Player> playersInArea = new ArrayList<>();
        for (Player player : parent.getPlayers()) {
            if (
                    player.getPos().x > location.get(0) && player.getPos().x < location.get(1)
                    && player.getPos().y > location.get(2) && player.getPos().y < location.get(3)
                    && player.isAlive()
            ) {
                playersInArea.add(player);
                if (playersInArea.size() > 1) break;
            }
        }
        if (playersInArea.size() == 1) {
            if (statisticsTracker.playerInArea == null
                    || !statisticsTracker.playerInArea.equals(playersInArea.get(0))) {
                statisticsTracker.playerInArea = playersInArea.get(0);
                for (VoConnection c : parent.getConnections()) {
                    c.sendTCP(new PlayerKothChange(playersInArea.get(0)));
                }
            }
        } else {
            statisticsTracker.playerInArea = null;
        }
    }
}

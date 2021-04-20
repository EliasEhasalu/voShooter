package ee.taltech.voshooter.networking.server.gamestate.gamemodes;

import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.LevelGenerator;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.server.gamestate.statistics.StatisticsTracker;

import java.util.Map;

public class GameModeManager {

    private final Game parent;
    private final StatisticsTracker statisticsTracker;
    public Map<Integer, Float> location;
    private final int gameMode;

    public GameModeManager(Game parent, StatisticsTracker tracker, int gameMode) {
        this.parent = parent;
        this.statisticsTracker = tracker;
        this.gameMode = gameMode;
        if (gameMode == 2) location = LevelGenerator.getKothLocation(parent.getCurrentMap());
    }

    public void update() {
        statisticsUpdates();
        if (gameMode == 2) updateKingOfTheHill();
    }

    public void statisticsUpdates() {
        statisticsTracker.sendUpdates();
    }

    public void updateKingOfTheHill() {
        for (Player player : parent.getPlayers()) {
            if (player.getPos().x < location.get(1) && player.getPos().x > location.get(0)
                    && player.getPos().y < location.get(3) && player.getPos().y > location.get(2)) {
                System.out.println("Trueeee");
            }
        }
    }
}

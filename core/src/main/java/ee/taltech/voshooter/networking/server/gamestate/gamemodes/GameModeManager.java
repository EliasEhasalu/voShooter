package ee.taltech.voshooter.networking.server.gamestate.gamemodes;

import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.statistics.StatisticsTracker;

public class GameModeManager {

    private final Game parent;
    private final StatisticsTracker statisticsTracker;
    private final int gameMode;

    public GameModeManager(Game parent, StatisticsTracker tracker, int gameMode) {
        this.parent = parent;
        this.statisticsTracker = tracker;
        this.gameMode = gameMode;
    }

    public void update() {
        statisticsUpdates();
    }

    public void statisticsUpdates() {
        statisticsTracker.sendUpdates();
    }
}

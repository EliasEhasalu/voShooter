package ee.taltech.voshooter.networking.server.gamestate.gamemodes;

import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.statistics.StatisticsTracker;

import java.util.Map;

public class GameModeManager {

    private final Game parent;
    private final StatisticsTracker statisticsTracker;
    public Map<Integer, Float> location;
    private final int gameMode;
    private GameMode gameModeManager;

    public GameModeManager(Game parent, StatisticsTracker tracker, int gameMode) {
        this.parent = parent;
        this.statisticsTracker = tracker;
        this.gameMode = gameMode;
        makeGameModeManager();
    }

    private void makeGameModeManager() {
        if (gameMode == 0) gameModeManager = new FunkyManager(statisticsTracker);
        if (gameMode == 1) gameModeManager = new FreeForAllManager(statisticsTracker);
        if (gameMode == 2) gameModeManager = new KingOfTheHillManager(parent, statisticsTracker);
    }

    public void update() {
        gameModeManager.update();
    }
}

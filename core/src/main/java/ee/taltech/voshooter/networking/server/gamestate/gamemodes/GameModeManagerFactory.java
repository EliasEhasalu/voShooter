package ee.taltech.voshooter.networking.server.gamestate.gamemodes;

import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.statistics.KingOfTheHillStatistics;
import ee.taltech.voshooter.networking.server.gamestate.statistics.StatisticsTracker;

public abstract class GameModeManagerFactory {
    public static GameMode makeGameModeManager(Game parent, StatisticsTracker statisticsTracker, int gameMode) {
        GameMode gameModeManager = null;
        if (gameMode == 0) gameModeManager = new FunkyManager(parent, statisticsTracker);
        if (gameMode == 1) gameModeManager = new FreeForAllManager(parent, statisticsTracker);
        if (gameMode == 2) gameModeManager = new KingOfTheHillManager(parent, statisticsTracker);
        return gameModeManager;
    }

    public static StatisticsTracker makeStatisticsTracker(Game parent, int gameMode) {
        StatisticsTracker statisticsTracker;
        if (gameMode == 2) statisticsTracker = new KingOfTheHillStatistics(parent);
        else statisticsTracker = new StatisticsTracker(parent);
        return statisticsTracker;
    }
}

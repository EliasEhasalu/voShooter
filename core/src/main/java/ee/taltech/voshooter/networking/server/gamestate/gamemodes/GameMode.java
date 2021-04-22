package ee.taltech.voshooter.networking.server.gamestate.gamemodes;

import ee.taltech.voshooter.networking.server.gamestate.statistics.StatisticsTracker;

public abstract class GameMode {

    protected StatisticsTracker statisticsTracker;

    public abstract void update();

    public abstract void statisticsUpdates();
}

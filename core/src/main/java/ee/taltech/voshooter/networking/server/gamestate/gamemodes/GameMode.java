package ee.taltech.voshooter.networking.server.gamestate.gamemodes;

import ee.taltech.voshooter.networking.server.gamestate.statistics.StatisticsTracker;

public abstract class GameMode {

    protected StatisticsTracker statisticsTracker;
    protected double timePassed = 0;

    public abstract void update();

    public abstract void calculateTimeLeft();

    public abstract void statisticsUpdates();
}

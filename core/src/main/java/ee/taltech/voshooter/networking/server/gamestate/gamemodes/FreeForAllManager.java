package ee.taltech.voshooter.networking.server.gamestate.gamemodes;

import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.statistics.StatisticsTracker;

public class FreeForAllManager extends GameMode {

    private final Game parent;

    public FreeForAllManager(Game parent, StatisticsTracker statisticsTracker) {
        this.parent = parent;
        this.statisticsTracker = statisticsTracker;
    }

    @Override
    public void update() {
        calculateTimeLeft();
        statisticsUpdates();
    }

    @Override
    public void calculateTimeLeft() {
        timePassed += 1 / Game.TICK_RATE_IN_HZ;
        if (timePassed >= parent.gameLength) parent.shutDown();
    }

    @Override
    public void statisticsUpdates() {
        statisticsTracker.sendUpdates();
    }
}

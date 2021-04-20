package ee.taltech.voshooter.networking.server.gamestate.gamemodes;

import ee.taltech.voshooter.networking.server.gamestate.statistics.StatisticsTracker;

public class FreeForAllManager extends GameMode {

    public FreeForAllManager(StatisticsTracker statisticsTracker) {
        this.statisticsTracker = statisticsTracker;
    }

    @Override
    public void update() {
        statisticsUpdates();
    }

    @Override
    public void statisticsUpdates() {
        statisticsTracker.sendUpdates();
    }
}

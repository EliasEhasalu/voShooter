package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.movingstrategy;

import ee.taltech.voshooter.networking.server.gamestate.player.Bot;

public interface MovingStrategy {

    int[] getMovementDirections();
    void setBot(Bot bot);
}

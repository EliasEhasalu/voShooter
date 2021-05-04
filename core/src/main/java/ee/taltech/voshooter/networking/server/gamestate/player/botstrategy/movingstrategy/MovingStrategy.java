package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.movingstrategy;

import ee.taltech.voshooter.networking.server.gamestate.player.Bot;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;

public interface MovingStrategy {

    int[] getMovementDirections(Player closestEnemy, boolean targetIsHitScanned);
    void setBot(Bot bot);
}

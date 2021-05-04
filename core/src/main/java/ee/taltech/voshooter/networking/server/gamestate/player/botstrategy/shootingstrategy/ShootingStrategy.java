package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.shootingstrategy;

import ee.taltech.voshooter.networking.server.gamestate.player.Bot;

public interface ShootingStrategy {

    boolean toShoot(boolean targetIsHitScanned);
    void setBot(Bot bot);
}

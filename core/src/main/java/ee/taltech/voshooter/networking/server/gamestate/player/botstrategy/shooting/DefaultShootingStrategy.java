package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.shooting;

import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.RayCaster;
import ee.taltech.voshooter.networking.server.gamestate.player.Bot;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.BotStrategy;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class DefaultShootingStrategy implements ShootingStrategy {

    private static final float REACTION_TIME = 0.3f;
    private static final float REACTION_GRACE_PERIOD = 0.15f;
    private final RayCaster rayCaster = new RayCaster();
    private BotStrategy parent;

    private Bot bot;
    private float timeToReaction = REACTION_TIME;

    private boolean aimIsLocked = false;

    @Override
    public boolean toShoot(boolean targetIsHitScanned) {
        handleReactionTime(targetIsHitScanned);
        return defaultFiringStrategy();
    }

    private void handleReactionTime(boolean targetIsHitScanned) {
        aimIsLocked = targetIsHitScanned;

        if (aimIsLocked) timeToReaction = max(-REACTION_GRACE_PERIOD, timeToReaction - Game.timeElapsed());
        else timeToReaction = min(REACTION_TIME, timeToReaction + Game.timeElapsed());
    }


    private boolean defaultFiringStrategy() {
        return (timeToReaction <= 0f && aimIsLocked);
    }

    @Override
    public void setBot(Bot bot) {
        this.bot = bot;
    }
}

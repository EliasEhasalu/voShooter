package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.shootingstrategy;

import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.RayCaster;
import ee.taltech.voshooter.networking.server.gamestate.player.Bot;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.BotStrategy;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class DefaultShootingStrategy implements ShootingStrategy {

    private static final float REACTION_TIME = 0.2f;
    private final RayCaster rayCaster = new RayCaster();
    private BotStrategy parent;

    private Bot bot;
    private float timeToReaction = REACTION_TIME;

    private boolean aimIsLocked = false;

    @Override
    public boolean toShoot(boolean targetIsHitScanned) {
        handleReactionTime(targetIsHitScanned);

        switch (bot.getInventory().getCurrentWeaponType()) {
            case RAILGUN:
                return railGunFiringStrategy();
            case MACHINE_GUN:
                return machineGunFiringStrategy();
            case ROCKET_LAUNCHER:
                return rocketLauncherFiringStrategy();
            default:
                return pistolFiringStrategy();
        }
    }

    private void handleReactionTime(boolean targetIsHitScanned) {
        aimIsLocked = targetIsHitScanned;

        if (aimIsLocked) timeToReaction = max(-REACTION_TIME, timeToReaction - Game.timeElapsed());
        else timeToReaction = min(REACTION_TIME, timeToReaction + (Game.timeElapsed() / 2));
    }


    private boolean pistolFiringStrategy() {
        return (timeToReaction <= 0f && aimIsLocked);
    }

    private boolean railGunFiringStrategy() {
        return (timeToReaction <= 0f && aimIsLocked);
    }

    private boolean machineGunFiringStrategy() {
        return (timeToReaction <= 0f && aimIsLocked);
    }

    private boolean rocketLauncherFiringStrategy() {
        return (timeToReaction <= 0f && aimIsLocked);
    }

    @Override
    public void setBot(Bot bot) {
        this.bot = bot;
    }
}

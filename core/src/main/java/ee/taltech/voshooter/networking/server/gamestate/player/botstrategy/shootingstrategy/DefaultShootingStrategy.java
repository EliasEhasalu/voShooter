package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.shootingstrategy;

import com.badlogic.gdx.physics.box2d.Body;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.RayCaster;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.RayCollision;
import ee.taltech.voshooter.networking.server.gamestate.player.Bot;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;

import java.util.HashSet;

import static java.lang.Math.max;

public class DefaultShootingStrategy implements ShootingStrategy {

    private static final float REACTION_TIME = 0.35f;
    private final RayCaster rayCaster = new RayCaster();

    private Bot bot;
    private float timeToReaction = REACTION_TIME;

    private Body lastTargetedBody = null;
    private Body targetedBody = null;

    @Override
    public boolean toShoot() {
        handleReactionTime();

        switch (bot.getInventory().getCurrentWeaponType()) {
            case PISTOL:
                return pistolFiringStrategy();
            case RAILGUN:
                return railGunFiringStrategy();
            case MACHINE_GUN:
                return machineGunFiringStrategy();
            case ROCKET_LAUNCHER:
                return rocketLauncherFiringStrategy();
            default:
                return false;
        }
    }

    private void handleReactionTime() {
        Body temp = targetedBody;
        targetedBody = getHitScannedTarget();
        lastTargetedBody = temp;

        if (
                targetedBody != null &&
                targetedBody.getUserData() instanceof Player
                && targetedBody == lastTargetedBody
        ) {
            timeToReaction = max(0f, timeToReaction - Game.timeElapsed());
        } else timeToReaction = REACTION_TIME;
    }


    private boolean pistolFiringStrategy() {
        return (timeToReaction <= 0f && targetIsHitScanned());
    }

    private boolean railGunFiringStrategy() {
        return (timeToReaction <= 0f && targetIsHitScanned());
    }

    private boolean machineGunFiringStrategy() {
        return (timeToReaction <= 0f && targetIsHitScanned());
    }

    private boolean rocketLauncherFiringStrategy() {
        return (timeToReaction <= 0f && targetIsHitScanned());
    }

    private Body getHitScannedTarget() {
        RayCollision collision = rayCaster.getFirstCollision(
                bot.getGame(),
                bot.getPos(),
                bot.getViewDirection(),
                50f,
                new HashSet<>()
        );

        Body b = null;
        if (collision != null) b = collision.getCollidedBody();

        return b;
    }

    private boolean targetIsHitScanned() {
        return (targetedBody != null && targetedBody.getUserData() instanceof Player);
    }

    @Override
    public void setBot(Bot bot) {
        this.bot = bot;
    }
}

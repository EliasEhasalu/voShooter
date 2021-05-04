package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.shootingstrategy;

import com.badlogic.gdx.physics.box2d.Body;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.RayCaster;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.RayCollision;
import ee.taltech.voshooter.networking.server.gamestate.player.Bot;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;

import java.util.HashSet;

public class DefaultShootingStrategy implements ShootingStrategy {

    private final RayCaster rayCaster = new RayCaster();
    private Bot bot;

    @Override
    public boolean toShoot() {
        switch (bot.getInventory().getCurrentWeaponType()) {
            case PISTOL:
                return targetIsHitScanned();
            case RAILGUN:
                return targetIsHitScanned();
            default:
                return false;
        }
    }

    private boolean targetIsHitScanned() {
        RayCollision collision = rayCaster.getFirstCollision(
                bot.getWorld(),
                bot.getPos(),
                bot.getViewDirection(),
                60f,
                new HashSet<Body>(){{ add(bot.getBody()); }}
        );

        Body b = null;
        if (collision != null) b = collision.getCollidedBody();

        return (b != null && b.getUserData() != bot && b.getUserData() instanceof Player);
    }

    @Override
    public void setBot(Bot bot) {
        this.bot = bot;
    }
}

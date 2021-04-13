package ee.taltech.voshooter.weapon.projectile;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;

public abstract class Bullet extends Projectile {

    /**
     * Construct the projectile.
     * @param type     The type of projectile.
     * @param owner    The player who shot the projectile.
     * @param pos      The position of the projectile.
     * @param vel      The velocity of the projectile.
     * @param lifeTime The time this projectile will live.
     */
    public Bullet(Type type, Player owner, Vector2 pos, Vector2 vel, float lifeTime) {
        super(type, owner, pos, vel, lifeTime);
    }
}

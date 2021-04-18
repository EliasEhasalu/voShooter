package ee.taltech.voshooter.weapon.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;

public class Fireball extends Bullet {

    public static final float RADIUS = 0.5f;
    private static final float SPEED = 30f;
    private static final float LIFE_TIME = 0.23f;
    private static final int DAMAGE = 3;

    /**
     * Construct the projectile.
     * @param owner    The player who shot the projectile.
     * @param pos      The position of the projectile.
     */
    public Fireball(Player owner, Vector2 pos, Vector2 dir) {
        super(Type.FIREBALL, owner, pos, dir.setLength(SPEED), LIFE_TIME);
    }

    @Override
    public void handleCollision(Fixture fix) {
        if (
                !(fix.getBody().getUserData() == owner)
                && (!(fix.getBody().getUserData() instanceof Projectile))
        ) {
            if (fix.getBody().getUserData() instanceof Player) {
                Player p = (Player) fix.getBody().getUserData();
                p.takeDamage(DAMAGE, this);
            }

            if (!(fix.getBody().getUserData() instanceof Player)) destroy();
        }
    }

    @Override
    protected void uponDestroy() {
    }

    @Override
    public Object getDamageSource() {
        return getOwner();
    }
}

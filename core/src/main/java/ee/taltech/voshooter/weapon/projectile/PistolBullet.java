package ee.taltech.voshooter.weapon.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;

public class PistolBullet extends Bullet {

    public static final float RADIUS = 0.05f;
    private static final float IMPULSE = 1;
    private static final float LIFE_TIME = 2f;
    private static final int DAMAGE = 20;

    public PistolBullet(Player owner, Vector2 pos, Vector2 dir) {
        super(Type.PISTOL_BULLET, owner, pos, dir.setLength(IMPULSE), LIFE_TIME);
    }

    @Override
    public void handleCollision(Fixture fix) {
        if (
                !(fix.getBody().getUserData() == owner)
                && (!(fix.isSensor()))
                && (!(fix.getBody().getUserData() instanceof Projectile))
        ) {
            if (fix.getBody().getUserData() instanceof Player) {
                Player p = (Player) fix.getBody().getUserData();
                p.takeDamage(DAMAGE, this);
            }
            destroy();
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

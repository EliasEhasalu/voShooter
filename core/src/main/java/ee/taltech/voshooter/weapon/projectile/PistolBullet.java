package ee.taltech.voshooter.weapon.projectile;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.networking.messages.Player;

public class PistolBullet extends Projectile {

    private static final float RAD = 0.1f;
    private static final float SPEED = 11f;

    public PistolBullet(Player owner, Vector2 pos, Vector2 dir) {
        super(Type.PISTOL_BULLET, owner, pos, dir.scl(SPEED));
    }

    @Override
    public void handleCollision(Object o) {
    }
}

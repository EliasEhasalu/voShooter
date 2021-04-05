package ee.taltech.voshooter.weapon.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import ee.taltech.voshooter.networking.messages.Player;

public class PistolBullet extends Projectile {

    private static final float RAD = 0.1f;
    private static final float SPEED = 11f;
    private static final float LIFE_TIME = 2f;

    public PistolBullet(Player owner, Vector2 pos, Vector2 dir) {
        super(Type.PISTOL_BULLET, owner, pos, dir.scl(SPEED), LIFE_TIME);
    }

    @Override
    public void handleCollision(Fixture fix) {
    }

    @Override
    protected void uponDestroy() {
    }
}

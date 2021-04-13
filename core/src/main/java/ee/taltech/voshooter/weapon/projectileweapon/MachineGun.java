package ee.taltech.voshooter.weapon.projectileweapon;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.weapon.projectile.PistolBullet;
import ee.taltech.voshooter.weapon.projectile.Projectile;

import java.util.Random;

public class MachineGun extends ProjectileWeapon {

    private static final float BASE_COOL_DOWN = 0.06f;
    private static final Random random = new Random();
    public static final int BULLET_SPREAD = 5;

    public MachineGun(Player owner) {
        super(owner, BASE_COOL_DOWN);
    }

    @Override
    public void fire() {
        if (canFire()) {
            remainingCoolDown = coolDown;
            final boolean randomDirection = random.nextBoolean();
            final float randomAngle;
            if (randomDirection) {
                randomAngle = random.nextInt(BULLET_SPREAD) * -1;
            } else {
                randomAngle = random.nextInt(BULLET_SPREAD);
            }

            Vector2 offset = wielder.getViewDirection().cpy().nor().rotateDeg(randomAngle);

            Projectile p = new PistolBullet(
                    wielder,
                    wielder.getPos().cpy().add(wielder.getViewDirection().cpy().setLength(PistolBullet.RADIUS)),
                    offset.cpy()
            );

            wielder.getGame().getEntityManagerHub().add(p);
        }
    }
}

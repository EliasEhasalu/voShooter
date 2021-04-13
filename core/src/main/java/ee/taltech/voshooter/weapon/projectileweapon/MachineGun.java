package ee.taltech.voshooter.weapon.projectileweapon;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.weapon.projectile.PistolBullet;
import ee.taltech.voshooter.weapon.projectile.Projectile;

import java.util.Random;

public class MachineGun extends ProjectileWeapon {

    private static final float BASE_COOL_DOWN = 0.06f;
    private static final Random random = new Random();
    public static final float BULLET_SPREAD = 10f;

    public MachineGun(Player owner) {
        super(owner, BASE_COOL_DOWN, Type.MACHINE_GUN);
    }

    @Override
    public void fire() {
        if (canFire()) {
            remainingCoolDown = coolDown;
            final float start = -BULLET_SPREAD / 2;
            final float angle = (random.nextInt(20) - BULLET_SPREAD) / 2;

            Vector2 offset = wielder.getViewDirection().cpy().nor().rotateDeg(start + angle);

            Projectile p = new PistolBullet(
                    wielder,
                    wielder.getPos().cpy().add(wielder.getViewDirection().cpy().setLength(PistolBullet.RADIUS)),
                    offset.cpy()
            );

            wielder.getGame().getEntityManagerHub().add(p);
        }
    }
}

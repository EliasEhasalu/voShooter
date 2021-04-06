package ee.taltech.voshooter.weapon.projectileweapon;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.weapon.projectile.Fireball;
import ee.taltech.voshooter.weapon.projectile.Projectile;

import java.util.Random;

public class Flamethrower extends ProjectileWeapon {

    private static final int FLAME_COUNT = 5;
    private static final float COOL_DOWN = 0.3f;
    private static final float CONE_ANGLE = 60f;
    private static final float FIREBALL_SPAWN_DISTANCE_VARIANCE = 2 * Fireball.RADIUS;
    private final Random rand = new Random();

    /**
     * @param wielder The player who wields this weapon.
     */
    public Flamethrower(Player wielder) {
        super(wielder, COOL_DOWN);
    }

    @Override
    public void fire() {
        if (canFire()) {
            remainingCoolDown = coolDown;

            for (int i = 0; i < FLAME_COUNT; i++) {
                final float start = -(CONE_ANGLE) / 2;
                final float end = (CONE_ANGLE) / 2;
                final float inc = (end - start) / FLAME_COUNT;
                final float dist = rand.nextFloat() * FIREBALL_SPAWN_DISTANCE_VARIANCE;

                Vector2 offset = wielder.getViewDirection().cpy().nor().rotateDeg(start + i * inc);

                Projectile p = new Fireball(
                        wielder,
                        wielder.getPos().cpy().add(offset.cpy().setLength(Fireball.RADIUS + dist)),
                        offset.cpy()
                );

                wielder.getGame().getEntityManagerHub().add(p);
            }
        }
    }
}

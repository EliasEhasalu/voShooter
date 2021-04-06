package ee.taltech.voshooter.weapon.projectileweapon;

import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.weapon.projectile.Fireball;
import ee.taltech.voshooter.weapon.projectile.Projectile;
import ee.taltech.voshooter.weapon.projectile.ShotgunPellet;

public class Flamethrower extends ProjectileWeapon {

    private static final int FLAME_COUNT = 8;
    private static final float COOL_DOWN = 0.1f;
    private static final float CONE_ANGLE = 60f;

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

                Vector2 offset = wielder.getViewDirection().cpy().nor().rotateDeg(start + i * inc);

                Projectile p = new Fireball(
                        wielder,
                        wielder.getPos().cpy().add(offset.cpy().setLength(Fireball.RADIUS)),
                        offset.cpy()
                );

                wielder.getGame().getEntityManagerHub().add(p);
            }
        }
    }
}

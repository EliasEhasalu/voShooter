package ee.taltech.voshooter.weapon;

import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.weapon.projectile.Projectile;
import ee.taltech.voshooter.weapon.projectile.Rocket;

public class RocketLauncher extends ProjectileWeapon {

    private static final float COOL_DOWN = 1;

    public RocketLauncher(Player wielder) {
        super(wielder, COOL_DOWN);
    }

    @Override
    public void fire() {
        if (canFire()) {
            remainingCoolDown = coolDown;

            Projectile p = new Rocket(
                    wielder,
                    wielder.getPos().cpy().add(wielder.getViewDirection().cpy().setLength(0.2f)),
                    wielder.getViewDirection().cpy().nor()
            );

            wielder.getGame().getEntityManagerHub().add(p);
        }
    }
}

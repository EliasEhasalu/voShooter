package ee.taltech.voshooter.weapon.projectile;

import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.weapon.ProjectileWeapon;

public class Pistol extends ProjectileWeapon {

    private static final float BASE_COOL_DOWN = 0.15f;

    public Pistol(Player owner) {
        super(owner, BASE_COOL_DOWN);
    }

    @Override
    public void fire() {
        if (canFire()) {
            remainingCoolDown = coolDown;

            Projectile p = new PistolBullet(
                    wielder,
                    wielder.getPos().cpy(),
                    wielder.getViewDirection().cpy().nor().scl(0.1f)
            );

            wielder.getGame().getEntityManagerHub().add(p);
        }
    }
}

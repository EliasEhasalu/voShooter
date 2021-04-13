package ee.taltech.voshooter.weapon.projectileweapon;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.weapon.projectile.PistolBullet;
import ee.taltech.voshooter.weapon.projectile.Projectile;

public class Pistol extends ProjectileWeapon {

    private static final float BASE_COOL_DOWN = 0.25f;

    public Pistol(Player owner) {
        super(owner, BASE_COOL_DOWN, Type.PISTOL);
    }

    @Override
    public void fire() {
        if (canFire()) {
            remainingCoolDown = coolDown;

            Projectile p = new PistolBullet(
                    wielder,
                    wielder.getPos().cpy().add(wielder.getViewDirection().cpy().setLength(PistolBullet.RADIUS)),
                    wielder.getViewDirection().cpy().nor()
            );

            wielder.getGame().getEntityManagerHub().add(p);
        }
    }
}

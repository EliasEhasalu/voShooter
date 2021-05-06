package ee.taltech.voshooter.weapon.projectileweapon;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.weapon.projectile.PistolBullet;
import ee.taltech.voshooter.weapon.projectile.Projectile;

public class Pistol extends ProjectileWeapon {

    private static final int STARTING_AMMO = Integer.MAX_VALUE;
    private static final float BASE_COOL_DOWN = 0.25f;

    public Pistol(Player owner) {
        super(owner, BASE_COOL_DOWN, STARTING_AMMO, Type.PISTOL);
    }

    @Override
    protected void onFire() {
        Projectile p = new PistolBullet(
                wielder,
                wielder.getPos().cpy().add(wielder.getViewDirection().cpy().setLength(PistolBullet.RADIUS)),
                wielder.getViewDirection().cpy().nor(), type
        );

        wielder.getGame().getEntityManagerHub().add(p);
    }

    @Override
    public int getMaxAmmo() {
        return STARTING_AMMO;
    }

    @Override
    public int getRemainingAmmo() {
       return STARTING_AMMO;
    }
}

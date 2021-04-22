package ee.taltech.voshooter.weapon.projectileweapon;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.weapon.projectile.Grenade;
import ee.taltech.voshooter.weapon.projectile.Projectile;

public class GrenadeLauncher extends ProjectileWeapon {

    private static final int STARTING_AMMO = 3;
    private static final float COOL_DOWN = 0.65f;

    public GrenadeLauncher(Player wielder) {
        super(wielder, COOL_DOWN, STARTING_AMMO, Type.ROCKET_LAUNCHER);
    }

    @Override
    protected void onFire() {
        Projectile p = new Grenade(
                wielder,
                wielder.getPos().cpy().add(wielder.getViewDirection().cpy().setLength(Grenade.RADIUS)),
                wielder.getViewDirection().cpy().nor()
        );

        wielder.getGame().getEntityManagerHub().add(p);
    }

    @Override
    public int getMaxAmmo() {
        return STARTING_AMMO;
    }
}

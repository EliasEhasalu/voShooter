package ee.taltech.voshooter.weapon.projectileweapon;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.weapon.projectile.Projectile;
import ee.taltech.voshooter.weapon.projectile.Rocket;

public class RocketLauncher extends ProjectileWeapon {

    private static final int STARTING_AMMO = 15;
    private static final float COOL_DOWN = 1f;

    public RocketLauncher(Player wielder) {
        super(wielder, COOL_DOWN, STARTING_AMMO, Type.ROCKET_LAUNCHER);
    }

    @Override
    protected void onFire() {
        Projectile p = new Rocket(
                wielder,
                wielder.getPos().cpy().add(wielder.getViewDirection().cpy().setLength(Rocket.RADIUS)),
                wielder.getViewDirection().cpy().nor()
        );

        wielder.getGame().getEntityManagerHub().add(p);
    }

    @Override
    public int getMaxAmmo() {
        return STARTING_AMMO;
    }
}

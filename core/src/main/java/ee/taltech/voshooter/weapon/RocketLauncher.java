package ee.taltech.voshooter.weapon;

import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.weapon.projectile.Projectile;
import ee.taltech.voshooter.weapon.projectile.Rocket;

public class RocketLauncher extends ProjectileWeapon {

    private static final float COOL_DOWN = 10 * (float) Game.TICK_RATE_IN_HZ;

    public RocketLauncher(Player wielder) {
        super(wielder, COOL_DOWN);
    }

    @Override
    public void fire() {
        if (canFire()) {
            Projectile p = new Rocket(wielder, wielder.getPos(), wielder.getViewDirection());
        }
    }
}

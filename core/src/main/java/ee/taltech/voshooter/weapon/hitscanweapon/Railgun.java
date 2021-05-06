package ee.taltech.voshooter.weapon.hitscanweapon;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.weapon.AmmoWeapon;

public class Railgun extends HitscanWeapon implements AmmoWeapon {

    private static final float COOL_DOWN = 1.6f;
    private static final float MAX_DIST = 50f;
    private static final int DAMAGE = 35;
    private static final int STARTING_AMMO = 10;

    public Railgun(Player wielder) {
        super(wielder, COOL_DOWN, STARTING_AMMO, MAX_DIST, DAMAGE, Type.RAILGUN);
    }

    @Override
    public int getMaxAmmo() {
        return STARTING_AMMO;
    }

    @Override
    public Object getDamageSource() {
        return wielder;
    }
}

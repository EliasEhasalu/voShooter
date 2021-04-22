package ee.taltech.voshooter.weapon.hitscanweapon;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.server.gamestate.player.status.DamageDealer;
import ee.taltech.voshooter.weapon.Weapon;

public abstract class HitscanWeapon extends Weapon implements DamageDealer {

    public HitscanWeapon(Player wielder, float coolDown, int startingAmmo, Weapon.Type type) {
        super(wielder, coolDown, startingAmmo, type);
    }
}

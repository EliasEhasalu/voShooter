package ee.taltech.voshooter.weapon.projectileweapon;

import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.weapon.Weapon;

public abstract class ProjectileWeapon extends Weapon {
    /**
     * @param coolDown The time between fired shots.
     */
    public ProjectileWeapon(Player wielder, float coolDown) {
        super(wielder, coolDown);
    }
}

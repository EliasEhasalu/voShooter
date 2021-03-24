package ee.taltech.voshooter.weapon;

import ee.taltech.voshooter.networking.messages.Player;

public abstract class ProjectileWeapon extends Weapon {
    /**
     * @param coolDown The time between fired shots.
     */
    public ProjectileWeapon(Player wielder, float coolDown) {
        super(wielder, coolDown);
    }
}

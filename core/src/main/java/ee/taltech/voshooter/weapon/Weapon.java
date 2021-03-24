package ee.taltech.voshooter.weapon;

import ee.taltech.voshooter.networking.messages.Player;

public abstract class Weapon {

    protected float coolDown;
    protected float remainingCoolDown;
    protected Player wielder;

    /**
     * @param coolDown The time between fired shots.
     */
    public Weapon(Player wielder, float coolDown) {
        this.wielder = wielder;
        this.coolDown = coolDown;
    }

    public boolean canFire() {
        return (remainingCoolDown == 0);
    }

    public abstract void fire();
}

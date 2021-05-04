package ee.taltech.voshooter.weapon;

import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;

import static java.lang.Math.max;
import static java.lang.Math.min;

public abstract class Weapon implements AmmoWeapon {

    protected float coolDown;
    protected float remainingCoolDown;
    protected Player wielder;
    protected Type type;
    protected int remainingAmmo;

    public enum Type {
        PISTOL,
        FLAMETHROWER,
        ROCKET_LAUNCHER,
        SHOTGUN,
        MACHINE_GUN,
        GRENADE_LAUNCHER,
        RAILGUN
    }

    /**
     * @param coolDown The time between fired shots.
     */
    public Weapon(Player wielder, float coolDown, int startingAmmo, Weapon.Type type) {
        this.wielder = wielder;
        this.coolDown = coolDown;
        this.remainingCoolDown = 0.1f;
        this.remainingAmmo = startingAmmo;
        this.type = type;
    }

    public boolean canFire() {
        return (
            remainingCoolDown == 0
            && remainingAmmo > 0
            && wielder.isAlive()
        );
    }

    public void coolDown() {
        remainingCoolDown = (float) max(0, remainingCoolDown - (1 / Game.TICK_RATE_IN_HZ));
    }

    public Type getType() {
        return type;
    }

    public void fire() {
        if (canFire()) {
            remainingCoolDown = coolDown;
            remainingAmmo = getRemainingAmmo() - 1;
            onFire();
        }
    }

    public void replenishAmmo() {
        remainingAmmo = getMaxAmmo();
    }

    public void replenishAmmoBy(float decimal) {
        // This nested chain of min(max()) is here to avoid integer overflow due to
        // the Pistol class having its max ammo denoted by a MAX_INTEGER.
        remainingAmmo = min(getMaxAmmo(), max(getRemainingAmmo(), getRemainingAmmo() + (int) Math.ceil(getMaxAmmo() * decimal)));
    }

    public int getRemainingAmmo() {
        return remainingAmmo;
    }

    protected abstract void onFire();
}

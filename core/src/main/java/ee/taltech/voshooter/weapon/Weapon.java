package ee.taltech.voshooter.weapon;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.server.gamestate.Game;

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
        MACHINE_GUN
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
        return (remainingCoolDown == 0 && remainingAmmo > 0);
    }

    public void coolDown() {
        remainingCoolDown = (float) Math.max(0, remainingCoolDown - (1 / Game.TICK_RATE_IN_HZ));
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

    public int getRemainingAmmo() {
        return remainingAmmo;
    }

    protected abstract void onFire();
}

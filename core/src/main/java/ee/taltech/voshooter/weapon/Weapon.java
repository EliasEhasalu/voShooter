package ee.taltech.voshooter.weapon;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.server.gamestate.Game;

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
        this.remainingCoolDown = 0.1f;
    }

    public boolean canFire() {
        return (remainingCoolDown == 0);
    }

    public void coolDown() {
        remainingCoolDown = (float) Math.max(0, remainingCoolDown - (1 / Game.TICK_RATE_IN_HZ));
    }

    public abstract void fire();
}

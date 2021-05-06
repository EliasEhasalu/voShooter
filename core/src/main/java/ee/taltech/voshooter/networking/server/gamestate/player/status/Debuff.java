package ee.taltech.voshooter.networking.server.gamestate.player.status;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.weapon.Weapon;

public abstract class Debuff {

    protected enum Type {
        BURNING
    }

    private int time;
    private final int frequency;
    private final Type type;
    protected final Player target;
    protected final Object source;
    protected final Weapon.Type weaponType;

    protected Debuff(Type type, Player target, Object source, int time, int frequency, Weapon.Type weaponType) {
        this.type = type;
        this.target = target;
        this.source = source;
        this.time = time;
        this.frequency = frequency;
        this.weaponType = weaponType;
    }

    protected Object getSource() {
        return source;
    }

    protected Type getType() {
        return type;
    }

    protected void update() {
        if (time % frequency == 0) applyEffect();
        if (time > -1) time--;
    }

    protected int getTimeLeft() {
        return time;
    }

    protected abstract void applyEffect();
}

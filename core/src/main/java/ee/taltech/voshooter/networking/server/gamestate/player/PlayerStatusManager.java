package ee.taltech.voshooter.networking.server.gamestate.player;

import ee.taltech.voshooter.networking.server.gamestate.Game;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatusManager {

    public enum Debuff {
        BURNING
    }

    private static final int HERTZ = ((int) Game.TICK_RATE_IN_HZ);

    private final Player parent;

    private Map<Debuff, Integer> debuffDurations = new HashMap<>();
    private Map<Debuff, Integer> maxDebuffDurations = new HashMap<Debuff, Integer>() {{
        put(Debuff.BURNING, 10 * HERTZ);
    }};

    public PlayerStatusManager(Player parent) {
        this.parent = parent;
    }

    public void applyDebuff(Debuff debuff) {
        debuffDurations.put(debuff, maxDebuffDurations.get(debuff));
    }

    public void update() {
        reduceDurations();
        applyEffects();
    }

    private void reduceDurations() {
        debuffDurations.replaceAll(((k, v) -> v - 1));
        for (Map.Entry<Debuff, Integer> e : debuffDurations.entrySet()) {
            if (e.getValue() <= 0) debuffDurations.remove(e.getKey());
        }
    }

    private void applyEffects() {
        for (Map.Entry<Debuff, Integer> e : debuffDurations.entrySet()) {
            if (e.getKey() == Debuff.BURNING) burn(parent);
        }
    }

    private void burn(Player parent) {
       final int FREQ = 32;
       final int DAMAGE = 2;

       if (debuffDurations.get(Debuff.BURNING) % FREQ == 0) {
           parent.takeDamage(DAMAGE);
       }
    }
}

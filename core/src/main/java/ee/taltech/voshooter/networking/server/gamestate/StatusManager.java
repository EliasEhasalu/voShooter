package ee.taltech.voshooter.networking.server.gamestate;

import ee.taltech.voshooter.networking.messages.Player;

import java.util.HashMap;
import java.util.Map;

public class StatusManager {

    public enum Debuff {
        BURNING
    }

    private final Player parent;
    private Map<Debuff, Float> debuffDurations = new HashMap<>();
    private Map<Debuff, Float> maxDebuffDurations = new HashMap<Debuff, Float>() {{
        put(Debuff.BURNING, 4f);
    }};

    public StatusManager(Player parent) {
        this.parent = parent;
    }

    public void applyDebuff(Debuff debuff) {
        debuffDurations.put(debuff, maxDebuffDurations.get(debuff));
    }

    public void tickDebuffs() {
        reduceDurations();
        applyEffects();
    }

    private void reduceDurations() {
        debuffDurations.replaceAll(((k, v) -> v - (1 / ((float) Game.TICK_RATE_IN_HZ))));
        for (Map.Entry<Debuff, Float> e : debuffDurations.entrySet()) {
            if (e.getValue() <= 0) debuffDurations.remove(e.getKey());
        }
    }

    private void applyEffects() {
        for (Map.Entry<Debuff, Float> e : debuffDurations.entrySet()) {
            switch (e.getKey()) {
                case BURNING:
                    parent.takeDamage(1);
                default:
                    // No op
            }
        }
    }
}

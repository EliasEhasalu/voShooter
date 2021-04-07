package ee.taltech.voshooter.networking.server.gamestate;

import ee.taltech.voshooter.networking.messages.Player;

import java.util.HashMap;
import java.util.Map;

public class StatusManager {

    public enum Debuff {
        BURNING
    }

    private static final int HERTZ = ((int) Game.TICK_RATE_IN_HZ);

    private final Player parent;
    private Player debuffApllier;
    private Map<Debuff, Integer> debuffDurations = new HashMap<>();
    private Map<Debuff, Integer> maxDebuffDurations = new HashMap<Debuff, Integer>() {{
        put(Debuff.BURNING, 10 * HERTZ);
    }};

    public StatusManager(Player parent) {
        this.parent = parent;
    }

    public void applyDebuff(Debuff debuff) {
        System.out.println("APPLYING BURN");
        debuffDurations.put(debuff, maxDebuffDurations.get(debuff));
    }

    public void update() {
        parent.getGame().sendUpdates = true;
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
            switch (e.getKey()) {
                case BURNING:
                    burn(parent);
                default:
                    // No op
            }
        }
    }

    private void burn(Player parent) {
       final int FREQ = 32;
       final int DAMAGE = 2;

       if (debuffDurations.get(Debuff.BURNING) % FREQ == 0) {
           parent.takeDamage(DAMAGE);
           if (parent.deathTick) {
               if (debuffApllier.equals(parent)) {
                   debuffApllier.removeKill();
               } else {
                   debuffApllier.addKill();
               }
           }
           debuffApllier.getGame().sendUpdates = true;
       }
    }
}

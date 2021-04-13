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
    private Player burnApplier;
    private Map<Debuff, Integer> debuffDurations = new HashMap<>();
    private Map<Debuff, Integer> maxDebuffDurations = new HashMap<Debuff, Integer>() {{
        put(Debuff.BURNING, 10 * HERTZ);
    }};

    public PlayerStatusManager(Player parent) {
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
            if (e.getKey() == Debuff.BURNING) burn(parent);
        }
    }

    private void burn(Player parent) {
       final int FREQ = 32;
       final int DAMAGE = 2;

       if (debuffDurations.get(Debuff.BURNING) % FREQ == 0) {
           parent.takeDamage(DAMAGE);
           if (parent.deathTick) {
               if (burnApplier.equals(parent)) {
                   burnApplier.removeKill();
               } else {
                   burnApplier.addKill();
               }
           }
           burnApplier.getGame().sendUpdates = true;
       }
    }

    public void setBurnApplier(Player burnApplier) {
        this.burnApplier = burnApplier;
    }
}

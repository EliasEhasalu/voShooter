package ee.taltech.voshooter.networking.server.gamestate.player.status;

import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatusManager {

    private static final int HERTZ = ((int) Game.TICK_RATE_IN_HZ);

    private final Player parent;
    private final Map<Debuff.Type, Debuff> debuffs = new HashMap<>();

    public PlayerStatusManager(Player parent) {
        this.parent = parent;
    }

    public void applyDebuff(Debuff debuff) {
        debuffs.put(debuff.getType(), debuff);
    }

    public void update() {
        debuffs.values().forEach(Debuff::update);
        for (Map.Entry<Debuff.Type, Debuff> e : debuffs.entrySet()) {
            if (e.getValue().getTimeLeft() <= 0) debuffs.remove(e.getKey());
        }
    }
}

package ee.taltech.voshooter.networking.server.gamestate.player.status;

import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.max;

public class PlayerStatusManager {

    private static final int HERTZ = ((int) Game.TICK_RATE_IN_HZ);
    private static final float MAX_DASH_COOL_DOWN = 10f;

    private final Player parent;
    private final Map<Debuff.Type, Debuff> debuffs = new HashMap<>();
    private float dashCoolDown = 0f;

    public PlayerStatusManager(Player parent) {
        this.parent = parent;
    }

    public void applyDebuff(Debuff debuff) {
        debuffs.put(debuff.getType(), debuff);
    }

    public void update() {
        trackDashCoolDown();
        updateDebuffs();
    }

    private void trackDashCoolDown() {
        dashCoolDown = max(0f, (float) (dashCoolDown - (1 / Game.TICK_RATE_IN_HZ)));
    }

    private void updateDebuffs() {
        debuffs.values().forEach(Debuff::update);
        forgetExpiredDebuffs();
    }

    private void forgetExpiredDebuffs() {
        for (Map.Entry<Debuff.Type, Debuff> e : debuffs.entrySet()) {
            if (e.getValue().getTimeLeft() <= 0) debuffs.remove(e.getKey());
        }
    }

    public boolean canDash() {
        return (dashCoolDown <= 0);
    }

    public void playerDashed() {
        dashCoolDown = MAX_DASH_COOL_DOWN;
    }

    public void resetCoolDowns() {
        dashCoolDown = 0f;
    }
}

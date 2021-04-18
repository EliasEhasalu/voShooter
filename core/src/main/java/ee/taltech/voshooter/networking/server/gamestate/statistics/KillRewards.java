package ee.taltech.voshooter.networking.server.gamestate.statistics;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;

public class KillRewards {

    protected static void applyKillRewards(Player killer) {
        killer.getStatusManager().resetCoolDowns();
        killer.getInventory().replenishWeaponAmmoBy(0.25f);
    }
}

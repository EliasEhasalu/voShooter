package ee.taltech.voshooter.networking.server.gamestate.statistics;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.server.gamestate.player.status.DamageDealer;

import java.util.HashMap;
import java.util.Map;

public class StatisticsTracker {

    private final Map<Player, DamageDealer> lastDamageTakenFrom = new HashMap<>();  // receiver <- dealer
    private final Map<Player, Integer> deathCount = new HashMap<>();
    private final Map<Player, Integer> killCount = new HashMap<>();

    public StatisticsTracker() {
    }

    public void setLastDamageTakenFrom(Player receiver, DamageDealer dealer) {
        lastDamageTakenFrom.put(receiver, dealer);
    }

    public void incrementDeaths(Player dyingPlayer) {
        deathCount.put(dyingPlayer, deathCount.getOrDefault(dyingPlayer, 0) + 1);

        Object killer = lastDamageTakenFrom.get(dyingPlayer);
        if (killer instanceof Player) killCount.put((Player) killer, deathCount.getOrDefault(dyingPlayer, 0) + 1);
    }

    public void sendUpdates() {
        // TODO
    }
}

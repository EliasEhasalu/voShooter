package ee.taltech.voshooter.networking.server.gamestate.statistics;

import ee.taltech.voshooter.networking.messages.clientreceived.PlayerDeath;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerStatistics;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerTookDamage;
import ee.taltech.voshooter.networking.server.VoConnection;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.server.gamestate.player.status.DamageDealer;
import ee.taltech.voshooter.weapon.Weapon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsTracker {

    protected static final int FREQUENCY = 60;

    protected int updateTicker = 0;
    private boolean existImportantUpdates = true;

    private final Map<Player, DamageDealer> lastDamageTakenFrom = new HashMap<>();  // receiver <- dealer
    private final Map<Player, Weapon.Type> lastDamageTakenFromWeaponType = new HashMap<>();
    protected final Map<Player, Integer> deathCount = new HashMap<>();
    protected final Map<Player, Integer> killCount = new HashMap<>();
    private final List<PlayerDeath> playerDeathEvents = new LinkedList<>();
    private final Deque<PlayerTookDamage> playerDamageEvents = new LinkedList<>();
    protected final Game parent;

    public StatisticsTracker(Game parent) {
        this.parent = parent;
    }

    public void setLastDamageTakenFrom(Player receiver, DamageDealer dealer, int amount, Weapon.Type type) {
        addPlayerDamageEvent(receiver, dealer, amount);
        lastDamageTakenFrom.put(receiver, dealer);
        lastDamageTakenFromWeaponType.put(receiver, type);
    }

    private void addPlayerDamageEvent(Player receiver, DamageDealer dealer, int amount) {
        if (dealer.getDamageSource() instanceof Player) {
            Player p = (Player) dealer.getDamageSource();
            playerDamageEvents.add(new PlayerTookDamage(amount, receiver.getId(), p.getId()));
        } else {
            playerDamageEvents.add(new PlayerTookDamage(amount, receiver.getId()));
        }
    }

    public void incrementDeaths(Player dyingPlayer) {
        setImportant();
        deathCount.put(dyingPlayer, deathCount.getOrDefault(dyingPlayer, 0) + 1);

        Object killer = lastDamageTakenFrom.get(dyingPlayer).getDamageSource();

        if (killer instanceof Player && killer != dyingPlayer) {
            Player killingPlayer = (Player) killer;
            KillRewards.applyKillRewards(killingPlayer);

            killCount.put(killingPlayer, killCount.getOrDefault(killingPlayer, 0) + 1);
            PlayerDeath death = new PlayerDeath(dyingPlayer.getId(), killingPlayer.getId(),
                    lastDamageTakenFromWeaponType.get(dyingPlayer));
            playerDeathEvents.add(death);
        } else if (killer instanceof Player) {
            playerDeathEvents.add(new PlayerDeath(dyingPlayer.getId()));
        }
    }

    public void sendUpdates() {
        if (existImportantUpdates || isPeriodicUpdate()) assembleUpdates();
        modulo();
        unsetImportant();
    }

    private void assembleUpdates() {
        sendPlayerStats();
        sendPlayerDeathEvents();
        sendPlayerDamageEvents();
    }

    private void sendPlayerDamageEvents() {
        while (!playerDamageEvents.isEmpty()) {
            for (VoConnection c : parent.getConnections()) {
                c.sendTCP(playerDamageEvents.peek());
            }
            playerDamageEvents.pop();
        }
    }

    private void sendPlayerStats() {
        for (VoConnection c : parent.getConnections()) {
            for (Player p : parent.getPlayers()) {
                int kills = killCount.getOrDefault(p, 0);
                int deaths = deathCount.getOrDefault(p, 0);
                c.sendTCP(new PlayerStatistics(p.getId(), deaths, kills, parent.getGameModeManager().getTimePassed()));
            }
        }
    }

    private void sendPlayerDeathEvents() {
        for (VoConnection c : parent.getConnections()) {
            for (PlayerDeath deathEvent : playerDeathEvents) {
                c.sendTCP(deathEvent);
            }
        }
        playerDeathEvents.clear();
    }

    private boolean isPeriodicUpdate() {
        return (updateTicker % FREQUENCY == 0);
    }

    private void modulo() {
        updateTicker++;
        updateTicker %= FREQUENCY;
    }

    private void setImportant() {
        this.existImportantUpdates = true;
    }

    private void unsetImportant() {
        this.existImportantUpdates = false;
    }

    public List<Player> getTopKiller() {
        return killCount.keySet().stream().filter(Player::isAlive)
                .sorted(Comparator.comparingInt(killCount::get).reversed()).collect(Collectors.toList());
    }

    public List<String> generateEndLeaderBoard() {
        List<String> leaderBoard = new ArrayList<>();
        List<Player> playerSet = parent.getPlayers().stream()
                .sorted(Comparator.comparingInt(player -> killCount.getOrDefault(player, 0)).reversed())
                .collect(Collectors.toList());
        for (Player player : playerSet) {
            int kills = killCount.getOrDefault(player, 0);
            int deaths = deathCount.getOrDefault(player, 0);
            String kdr;
            if (deaths != 0) kdr = String.valueOf((double) Math.round((kills / (double) deaths) * 100) / 100);
            else kdr = String.valueOf(kills);
            leaderBoard.add(String.format("%-12s Kills: %-3d Deaths: %-3d Kdr: %-5s", player.getName(),
                    killCount.getOrDefault(player, 0), deathCount.getOrDefault(player, 0), kdr));
        }
        return leaderBoard;
    }
}

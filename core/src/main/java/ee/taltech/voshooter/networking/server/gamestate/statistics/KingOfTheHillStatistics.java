package ee.taltech.voshooter.networking.server.gamestate.statistics;

import ee.taltech.voshooter.networking.messages.clientreceived.PlayerKothScores;
import ee.taltech.voshooter.networking.server.VoConnection;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class KingOfTheHillStatistics extends StatisticsTracker {

    public Player playerInArea;
    private Map<Long, Double> kothPlayers = new HashMap<>();

    public KingOfTheHillStatistics(Game parent) {
        super(parent);
    }

    @Override
    public void sendUpdates() {
        refreshPlayers();
        updatePlayerScoreKoth();
        sendPlayerScoreKoth();
        super.sendUpdates();
    }

    public OptionalDouble getHighestTimeHeld() {
        return kothPlayers.values().stream().mapToDouble(value -> value).max();
    }

    public void refreshPlayers() {
        Map<Long, Double> newMap = new HashMap<>();
        for (Player player : parent.getPlayers()) {
            newMap.put(player.getId(), kothPlayers.getOrDefault(player.getId(), 0.0));
        }
        kothPlayers = newMap;
    }

    public void updatePlayerScoreKoth() {
        if (playerInArea != null) {
            kothPlayers.put(playerInArea.getId(), kothPlayers.get(playerInArea.getId()) + 1 / (double) FREQUENCY);
        }
    }

    public void sendPlayerScoreKoth() {
        if (updateTicker % (FREQUENCY / 3) == 0) {
            for (VoConnection c : parent.getConnections()) {
                c.sendTCP(new PlayerKothScores(kothPlayers));
            }
        }
    }

    @Override
    public List<Player> getTopKiller() {
        return parent.getPlayers().stream().filter(Player::isAlive)
                .sorted(Comparator.comparingDouble(p -> kothPlayers.getOrDefault(((Player) p).getId(), 0.0))
                        .reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> generateEndLeaderBoard() {
        List<String> leaderBoard = new ArrayList<>();
        List<Player> playerSet = parent.getPlayers();
        List<Map.Entry<Long, Double>> list = new ArrayList<>(kothPlayers.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);
        Optional<Player> playerOptional;
        for (Map.Entry<Long, Double> entry : list) {
            playerOptional = playerSet.stream().filter(player -> player.getId() == entry.getKey()).findFirst();
            if (playerOptional.isPresent()) {
                Player player = playerOptional.get();
                int kills = killCount.getOrDefault(player, 0);
                int deaths = deathCount.getOrDefault(player, 0);
                String kdr;
                if (deaths != 0) kdr = String.valueOf((double) Math.round((kills / (double) deaths) * 100) / 100);
                else kdr = String.valueOf(kills);
                leaderBoard.add(String
                        .format("%-12s KotH: %-4s Kills: %-3d Deaths: %-3d Kdr: %-5s",
                                player.getName(),
                                Math.round(kothPlayers.get(player.getId()) * 10f) / 10f,
                                killCount.getOrDefault(player, 0),
                                deathCount.getOrDefault(player, 0),
                                kdr));
            }
        }
        return leaderBoard;
    }
}

package ee.taltech.voshooter.networking.server.gamestate.entitymanager;

import com.badlogic.gdx.physics.box2d.World;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerDead;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerDeath;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerHealthUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerPositionUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerStatistics;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerViewUpdate;
import ee.taltech.voshooter.networking.server.VoConnection;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.PixelToSimulation;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager extends EntityManager {

    private final Set<Player> players = ConcurrentHashMap.newKeySet();

    public boolean sendUpdates;
    private int playerUpdateTick = 0;

    /**
     * Default constructor.
     * @param world
     * @param game
     */
    public PlayerManager(World world, Game game) {
        super(world, game);
    }

    @Override
    public void update() {
        players.forEach(Player::update);
    }

    @Override
    public void sendUpdates() {
        for (VoConnection c : game.getConnections()) {
            for (Player p : players) {
                c.sendTCP(new PlayerPositionUpdate(PixelToSimulation.toPixels(p.getPos()), p.getId()));
                c.sendTCP(new PlayerViewUpdate(p.getViewDirection(), p.getId()));
                if (sendUpdates || playerUpdateTick % 5 == 0) {
                    c.sendTCP(new PlayerHealthUpdate(p.getHealth(), p.getId()));
                    c.sendTCP(new PlayerStatistics(p.getId(), p.getDeaths(), p.getKills()));
                    sendUpdates = false;
                }
                if (p.deathTick) {
                    c.sendTCP(new PlayerDeath(p.getId(), p.getKillerId()));
                }
                if (p.getHealth() <= 0) {
                    c.sendTCP(new PlayerDead(p.getId(), p.getRespawnTime()));
                }
            }
            playerUpdateTick++;
        }

        players.forEach(p -> p.deathTick = false);
    }

    @Override
    public void add(Object player) {
        Player p  = (Player) player;

        players.add(p);
    }
}

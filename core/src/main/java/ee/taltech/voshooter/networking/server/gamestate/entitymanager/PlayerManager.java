package ee.taltech.voshooter.networking.server.gamestate.entitymanager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerDead;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerHealthUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerPositionUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerViewUpdate;
import ee.taltech.voshooter.networking.server.VoConnection;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.PixelToSimulation;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.ShapeFactory;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager extends EntityManager {

    private final Set<Player> players = ConcurrentHashMap.newKeySet();
    private final PlayerSpawner playerSpawner = new PlayerSpawner();

    public PlayerManager(World world, Game game) {
        super(world, game);
    }

    protected void createPlayer(VoConnection c) {
        Player p = new Player(this, c, c.user.id, c.user.getName());
        Body body = ShapeFactory.getPlayer(world, playerSpawner.getSpawnPointForCurrentMap());

        p.setBody(body);
        body.setUserData(p);

        // Set the player object on the connection.
        c.player = p;
        c.player.initialPos = body.getPosition();

        players.add(p);
    }

    @Override
    protected void update() {
        players.forEach(Player::update);
    }

    @Override
    protected void sendUpdates() {
        for (VoConnection c : game.getConnections()) {
            for (Player p : players) {
                c.sendTCP(new PlayerHealthUpdate(p.getHealth(), p.getId()));
                c.sendTCP(new PlayerPositionUpdate(PixelToSimulation.toPixels(p.getPos()), p.getId()));
                c.sendTCP(new PlayerViewUpdate(p.getViewDirection(), p.getId()));
                if (!(p.isAlive())) c.sendTCP(new PlayerDead(p.getId(), p.getTimeToRespawn()));
            }
        }
    }

    @Override
    public void add(Object player) {
        Player p = (Player) player;

        players.add(p);
    }

    public World getWorld() {
        return world;
    }

    public Game getGame() {
        return game;
    }

    public Vector2 getSpawnPoint() {
        return playerSpawner.getSpawnPointForCurrentMap();
    }

    public Set<Player> getPlayers() {
        return players;
    }
}

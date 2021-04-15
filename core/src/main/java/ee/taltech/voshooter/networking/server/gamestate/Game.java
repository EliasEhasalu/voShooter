package ee.taltech.voshooter.networking.server.gamestate;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.headless.HeadlessFileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerAction;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerInput;
import ee.taltech.voshooter.networking.server.VoConnection;
import ee.taltech.voshooter.networking.server.gamestate.collision.CollisionHandler;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.HijackedTmxLoader;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.LevelGenerator;
import ee.taltech.voshooter.networking.server.gamestate.entitymanager.EntityManagerHub;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.server.gamestate.statistics.StatisticsTracker;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Game extends Thread {

    private boolean running = false;

    public static final double TICK_RATE_IN_HZ = 60.0;
    private static final double TICK_RATE = 1000000000.0 / TICK_RATE_IN_HZ;

    private final Map<VoConnection, Set<PlayerAction>> connectionInputs = new ConcurrentHashMap<>();

    static {
        World.setVelocityThreshold(0.1f);
    }

    private TiledMap currentMap;
    private final World world = new World(new Vector2(0, 0), false);

    private final EntityManagerHub entityManagerHub = new EntityManagerHub(world, this);
    private final CollisionHandler collisionHandler = new CollisionHandler(world, this);
    private final InputHandler inputHandler = new InputHandler();
    private final StatisticsTracker statisticsTracker = new StatisticsTracker(this);

    /**
     * Construct the game.
     * @param gameMode The game mode for the game.
     */
    public Game(int gameMode) {
        setCurrentMap(gameMode);
        LevelGenerator.generateLevel(world, currentMap);

        world.setContactListener(collisionHandler);
    }

    /**
     * Add a connection to this game.
     * @param connection The connection to add.
     */
    public void addConnection(VoConnection connection) {
        // Track the connection.
        if (!connectionInputs.containsKey(connection)) connectionInputs.put(connection, ConcurrentHashMap.newKeySet());

        // Create a player object with a physics body which will be tracked in the world.
        entityManagerHub.createPlayer(connection);
    }

    /**
     * Remove a connection from this game.
     * @param connection the connection to remove.
     */
    public void removeConnection(VoConnection connection) {
        if (connectionInputs.containsKey(connection)) {
            connectionInputs.remove(connection);
            entityManagerHub.remove(connection.getID());
        }
    }

    /** Main game logic. */
    private void tick() {
        connectionInputs.forEach(this::handleInputs);     // Handle inputs.
        entityManagerHub.update();                        // Update logic.

        world.step((float) (1 / TICK_RATE_IN_HZ), 8, 4);  // Update physics simulation.

        entityManagerHub.sendUpdates();                   // Send updates to players.
        statisticsTracker.sendUpdates();
        clearPlayerInputs();                              // Clear inputs.
    }

    /**
     * React to the players' inputs.
     * @param c The connection which performed the inputs.
     * @param actions The actions they wish to take.
     */
    private void handleInputs(VoConnection c, Set<PlayerAction> actions) {
        try {
            actions.forEach(a -> {
                inputHandler.handleInput(c, a);
            });
        } catch (ConcurrentModificationException ignored) {
        }
    }

    /**
     * Add inputs to be dealt with next tick.
     * @param c The connection the input came from.
     * @param input The actions the player requests.
     */
    public void addPlayerInput(VoConnection c, PlayerInput input) {
        if (connectionInputs.containsKey(c)) {
            try {
                connectionInputs.get(c).addAll(input.inputs);
            } catch (ConcurrentModificationException ignored) {
            }
        }
    }

    /** Reset "last input" for every connection after every tick. */
    private void clearPlayerInputs() {
        connectionInputs.replaceAll((k, v) -> new HashSet<>());
    }

    /**
     * Start the game simulation.
     */
    @Override
    public void run() {
        running = true;
        double delta = 0;

        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / TICK_RATE;
            lastTime = now;

            while (delta >= 1) {
                tick();
                delta--;
            }
        }
    }

    /** Close the game simulation. */
    public void shutDown() {
        running = false;
    }

    /**
     * @return A list of player objects in this game.
     */
    public List<Player> getPlayers() {
       return connectionInputs.keySet().stream()
               .map(c -> c.player)
               .collect(Collectors.toList());
    }

    public Set<VoConnection> getConnections() {
        return connectionInputs.keySet();
    }

    public EntityManagerHub getEntityManagerHub() {
        return entityManagerHub;
    }

    public StatisticsTracker getStatisticsTracker() {
        return statisticsTracker;
    }

    private void setCurrentMap(int gameMode) {
        if (gameMode == 1) {
            currentMap = new HijackedTmxLoader(fileName -> new HeadlessFileHandle(fileName, Files.FileType.Classpath))
                    .load("tileset/vo_shooter_map.tmx");
        }

        // TODO add set map to entity managers
    }
}

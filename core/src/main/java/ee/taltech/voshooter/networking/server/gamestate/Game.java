package ee.taltech.voshooter.networking.server.gamestate;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.headless.HeadlessFileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ee.taltech.voshooter.map.GameMap;
import ee.taltech.voshooter.networking.messages.clientreceived.ChatGamePlayerChange;
import ee.taltech.voshooter.networking.messages.clientreceived.GameEnd;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerAction;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerInput;
import ee.taltech.voshooter.networking.server.VoConnection;
import ee.taltech.voshooter.networking.server.gamestate.collision.CollisionHandler;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.HijackedTmxLoader;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.LevelGenerator;
import ee.taltech.voshooter.networking.server.gamestate.entitymanager.EntityManagerHub;
import ee.taltech.voshooter.networking.server.gamestate.gamemodes.GameMode;
import ee.taltech.voshooter.networking.server.gamestate.gamemodes.GameModeManagerFactory;
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

    private final GameMap.MapType mapType;
    private TiledMap currentMap;
    public final int gameLength;
    private final World world = new World(new Vector2(0, 0), false);

    private final EntityManagerHub entityManagerHub = new EntityManagerHub(world, this);
    private final CollisionHandler collisionHandler = new CollisionHandler(world, this);
    private final InputHandler inputHandler = new InputHandler();
    private final StatisticsTracker statisticsTracker = new StatisticsTracker(this);
    private final GameMode gameModeManager;

    /**
     * Construct the game.
     * @param gameMode The game mode for the game.
     * @param mapType The game map used in this game.
     * @param gameLength Length of the round.
     */
    public Game(int gameMode, GameMap.MapType mapType, int gameLength) {
        this.mapType = mapType;
        if (gameLength >= 15) this.gameLength = gameLength;
        else this.gameLength = Integer.MAX_VALUE;
        setCurrentMap();
        gameModeManager = GameModeManagerFactory.makeGameModeManager(this, statisticsTracker, gameMode);
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
        for (VoConnection c : connectionInputs.keySet()) {
            if (!c.equals(connection)) {
                c.sendTCP(new ChatGamePlayerChange(String.format("Player %s has joined the game.",
                        connection.player.getName())));
            }
        }
    }

    /**
     * Remove a connection from this game.
     * @param connection the connection to remove.
     */
    public void removeConnection(VoConnection connection) {
        if (connectionInputs.containsKey(connection)) {
            connectionInputs.remove(connection);
            entityManagerHub.removePlayer(connection.user.id);
            for (VoConnection c : connectionInputs.keySet()) {
                if (!c.equals(connection)) {
                    c.sendTCP(new ChatGamePlayerChange(String.format("Player %s has left the game.",
                    connection.player.getName())));
                }
            }
        }
    }

    /** Main game logic. */
    private void tick() {
        connectionInputs.forEach(this::handleInputs);     // Handle inputs.
        entityManagerHub.update();                        // Update logic.
        gameModeManager.update();                         // Update game mode logic.

        world.step((float) (1 / TICK_RATE_IN_HZ), 8, 4);  // Update physics simulation.

        entityManagerHub.sendUpdates();                   // Send updates to players.
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
        for (VoConnection c : getConnections()) {
            c.sendTCP(new GameEnd());
        }
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

    public GameMap.MapType getMapType() {
        return mapType;
    }

    public EntityManagerHub getEntityManagerHub() {
        return entityManagerHub;
    }

    public StatisticsTracker getStatisticsTracker() {
        return statisticsTracker;
    }

    private void setCurrentMap() {
        currentMap = new HijackedTmxLoader(fileName -> new HeadlessFileHandle(fileName, Files.FileType.Classpath))
                .load(GameMap.getTileSet(mapType));
    }

    public TiledMap getCurrentMap() {
        return currentMap;
    }
}

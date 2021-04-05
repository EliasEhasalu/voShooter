package ee.taltech.voshooter.networking.server.gamestate;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import ee.taltech.voshooter.geometry.Pos;
import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerHealthUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerPositionUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerViewUpdate;
import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.messages.serverreceived.MovePlayer;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerAction;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerInput;
import ee.taltech.voshooter.networking.messages.serverreceived.Shoot;
import ee.taltech.voshooter.networking.server.VoConnection;
import ee.taltech.voshooter.networking.server.gamestate.collision.CollisionHandler;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.HijackedTmxLoader;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.MyFileHandleResolver;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.PixelToSimulation;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.ShapeFactory;
import ee.taltech.voshooter.networking.server.gamestate.entitymanager.EntityManagerHub;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Game extends Thread {

    private boolean running = false;

    public static final double TICK_RATE_IN_HZ = 64.0;
    private static final double TICK_RATE = 1000000000.0 / TICK_RATE_IN_HZ;

    private final Map<VoConnection, Set<PlayerAction>> connectionInputs = new ConcurrentHashMap<>();

    static {
        World.setVelocityThreshold(0.1f);
    }

    private TiledMap currentMap;
    private final World world = new World(new Vector2(0, 0), false);

    private final EntityManagerHub entityManagerHub = new EntityManagerHub(world, this);
    private final CollisionHandler collisionHandler = new CollisionHandler(world, this);

    /**
     * Construct the game.
     * @param gameMode The game mode for the game.
     */
    public Game(int gameMode) {
        setCurrentMap(gameMode);
        generateTerrain();

        world.setContactListener(collisionHandler);
    }

    /**
     * Add a connection to this game.
     * @param connection The connection to add.
     */
    public void addConnection(VoConnection connection) {
        // Track the connection.
        connectionInputs.computeIfAbsent(connection, k -> ConcurrentHashMap.newKeySet());

        // Create a player object with a physics body which will be tracked in the world.
        createPlayer(connection);
    }

    /**
     * Create a player object for a given connection.
     * @param c The connection to create the player for.
     */
    private void createPlayer(VoConnection c) {
        Player p = new Player(this, c.user.id, c.user.getName());
        Body body = ShapeFactory.getPlayer(world, getSpawnPoint());

        p.setBody(body);
        body.setUserData(p);

        // Set the player object on the connection.
        c.player = p;
        c.player.initialPos = body.getPosition();
    }

    /**
     * @return A spawn point for a player.
     */
    private Pos getSpawnPoint() {
        return new Pos(4, 4);
    }

    /**
     * @param connection The connection the update was received from.
     * @param update The mouse movement update.
     */
    private void updatePlayerDirection(VoConnection connection, MouseCoords update) {
        connection.player.setViewDirection(update);
    }

    /** Main game logic. */
    private void tick() {
        connectionInputs.forEach(this::handleInputs);
        connectionInputs.keySet().forEach(c -> c.player.update());

        entityManagerHub.update();

        world.step((float) (1 / TICK_RATE_IN_HZ), 8, 4);

        sendUpdatesToPlayers();
        clearPlayerInputs();
    }

    private void sendUpdatesToPlayers() {
        sendPlayerPoseUpdates();
        entityManagerHub.sendUpdates();
    }

    /**
     * React to the players' inputs.
     * @param c The connection which performed the inputs.
     * @param actions The actions they wish to take.
     */
    private void handleInputs(VoConnection c, Set<PlayerAction> actions) {
        actions.forEach(a -> {
            if (c.getPlayer().getHealth() > 0) {
                if (a instanceof MovePlayer) {
                    c.getPlayer().addMoveDirection(((MovePlayer) a).xDir, ((MovePlayer) a).yDir);
                } else if (a instanceof Shoot) {
                    c.getPlayer().shoot();
                } else if (a instanceof MouseCoords) {
                    c.getPlayer().setViewDirection((MouseCoords) a);
                }
            }
        });
    }

    /**
     * Respawn given player.
     * @param c the connection that needs to be respawned.
     */
    public void handleRespawn(VoConnection c) {
        c.getPlayer().respawn(getSpawnPoint(), 0f);
    }

    /**
     * Add inputs to be dealt with next tick.
     * @param c The connection the input came from.
     * @param input The actions the player requests.
     */
    public void addPlayerInput(VoConnection c, PlayerInput input) {
        if (connectionInputs.containsKey(c)) {
            connectionInputs.get(c).addAll(input.inputs);
        }
    }

    /**
     * Send position updates to players.
     */
    private void sendPlayerPoseUpdates() {
        for (VoConnection c : connectionInputs.keySet()) {
            for (Player p : getPlayers()) {
                c.sendTCP(new PlayerPositionUpdate(PixelToSimulation.toPixels(p.getPos()), p.getId()));
                c.sendTCP(new PlayerViewUpdate(p.getViewDirection(), p.getId()));
                c.sendTCP(new PlayerHealthUpdate(p.getHealth(), p.getId()));
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

    public World getWorld() {
        return world;
    }

    public Set<VoConnection> getConnections() {
        return connectionInputs.keySet();
    }

    public EntityManagerHub getEntityManagerHub() {
        return entityManagerHub;
    }

    private void setCurrentMap(int gameMode) {
        if (gameMode == 1) {
            currentMap = new HijackedTmxLoader(new MyFileHandleResolver())
                    .load("./core/assets/tileset/voShooterMap.tmx");
        }
    }

    /**
     * Add the collidable objects from the tiled map to the world object.
     */
    private void generateTerrain() {
        MapObjects objects;

        for (MapLayer l : currentMap.getLayers()) {
            if (l.getName().equals("WallsObjects")) {
                objects = l.getObjects();
                for (MapObject object : objects) {
                    Shape shape;

                    if (object instanceof RectangleMapObject) {
                        shape = ShapeFactory.getRectangle((RectangleMapObject) object);
                    } else if (object instanceof PolygonMapObject) {
                        shape = ShapeFactory.getPolygon((PolygonMapObject) object);
                    } else if (object instanceof PolylineMapObject) {
                        shape = ShapeFactory.getPolyline((PolylineMapObject) object);
                    } else if (object instanceof CircleMapObject) {
                        shape = ShapeFactory.getCircle((CircleMapObject) object);
                    } else {
                        continue;
                    }

                    BodyDef bodyDef = new BodyDef();
                    bodyDef.type = BodyDef.BodyType.StaticBody;
                    Body body = world.createBody(bodyDef);
                    Fixture f = body.createFixture(shape, 1);
                    f.setFriction(0f);
                    f.setRestitution(1f);

                    shape.dispose();
                }
                break;
            }
        }
    }
}

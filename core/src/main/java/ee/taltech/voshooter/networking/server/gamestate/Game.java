package ee.taltech.voshooter.networking.server.gamestate;

import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerPositionUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerViewUpdate;
import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.messages.serverreceived.MovePlayer;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerAction;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerInput;
import ee.taltech.voshooter.networking.messages.serverreceived.Shoot;
import ee.taltech.voshooter.networking.server.VoConnection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Game extends Thread {

    public static final double TICK_RATE_IN_HZ = 64.0;
    private static final double TICK_RATE = 1000000000.0 / TICK_RATE_IN_HZ;

    private static final float DRAG_CONSTANT = 0.9f;

    private final Map<VoConnection, Set<PlayerAction>> connectionInputs = new HashMap<>();
    private boolean running = false;

    /**
     * Add a connection to this game.
     * @param connection The connection to add.
     */
    public void addConnection(VoConnection connection) {
        connectionInputs.computeIfAbsent(connection, k -> new HashSet<>());
    }

    /**
     * @param connection The connection the update was received from.
     * @param update The mouse movement update.
     */
    public void updatePlayerDirection(VoConnection connection, MouseCoords update) {
        connection.player.setViewDirection(update);
    }

    /** Main game logic. */
    private void tick() {
        // Handle each player's inputs.
        connectionInputs.forEach(this::handleInputs);

        // Move players.
        connectionInputs.keySet().forEach(c -> {
            c.player.drag(DRAG_CONSTANT);
            c.player.move();
        });

        // Forget all inputs received since last tick.
        sendPlayerPoseUpdates();
        clearPlayerInputs();
    }

    /**
     * React to the players' inputs.
     * @param c The connection which performed the inputs.
     * @param actions The actions they wish to take.
     */
    private void handleInputs(VoConnection c, Set<PlayerAction> actions) {
        actions.forEach(a -> {
                if (a instanceof MovePlayer) {
                    c.getPlayer().addMoveDirection(((MovePlayer) a).xDir, ((MovePlayer) a).yDir);
                } else if (a instanceof Shoot) {
                    c.getPlayer().shoot();
                } else if (a instanceof MouseCoords) {
                    c.getPlayer().setViewDirection((MouseCoords) a);
                }
        });
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
        List<Player> players = connectionInputs.keySet().stream()
                .map(c -> c.player)
                .collect(Collectors.toList());

        for (VoConnection c : connectionInputs.keySet()) {
            for (Player p : players) {
                c.sendTCP(new PlayerPositionUpdate(p.getPos(), p.getId()));
                c.sendTCP(new PlayerViewUpdate(p.getViewDirection(), p.getId()));
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

    /**
     * @return A list of player objects in this game.
     */
    public List<Player> getPlayers() {
       return connectionInputs.keySet().stream()
               .map(c -> c.player)
               .collect(Collectors.toList());
    }

    /** Close the game simulation. */
    public void shutDown() {
        running = false;
    }
}

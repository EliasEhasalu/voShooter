package ee.taltech.voshooter.networking.server.gamestate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ee.taltech.voshooter.controller.PlayerAction;
import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerPositionUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerViewUpdate;
import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerInput;
import ee.taltech.voshooter.networking.server.VoConnection;

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
     * Add an update to be calculated next tick.
     * @param connection The connection the update came from.
     * @param update     The update to add.
     */
    public void handlePlayerInput(VoConnection connection, Object update) {
        if (update instanceof MouseCoords) {
            updatePlayerDirection(connection, (MouseCoords) update);
        } else if (update instanceof PlayerInput && connectionInputs.containsKey(connection)) {
            connectionInputs.get(connection).addAll(((PlayerInput) update).inputs);
        }
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
     * Delegate input handling to sub-functions.
     * @param c The connection the input came from.
     * @param actions The action the player requests.
     */
    private void handleInputs(VoConnection c, Set<PlayerAction> actions) {
        actions.forEach(a -> {
            if (a == PlayerAction.MOVE_LEFT) {
                c.player.addMoveDirection(-1, 0);
            } else if (a == PlayerAction.MOVE_RIGHT) {
                c.player.addMoveDirection(1, 0);
            } else if (a == PlayerAction.MOVE_UP) {
                c.player.addMoveDirection(0, 1);
            } else if (a == PlayerAction.MOVE_DOWN) {
                c.player.addMoveDirection(0, -1);
            }
        });
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

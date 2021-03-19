package ee.taltech.voshooter.networking.server.gamestate;

import java.util.*;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.controller.PlayerAction;
import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerPositionUpdate;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerInput;
import ee.taltech.voshooter.networking.server.VoConnection;

public class Game extends Thread {

    private static final double TICK_RATE_IN_HZ = 64.0;
    private static final double TICK_RATE = 1000000000.0 / TICK_RATE_IN_HZ;

    private static final float DRAG_CONSTANT = 0.85f;
    private static final float BASE_MOVEMENT_SPEED = (float) (120.0f / TICK_RATE_IN_HZ);

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
    public void addUpdate(VoConnection connection, PlayerInput update) {
        if (connectionInputs.containsKey(connection)) {
            connectionInputs.get(connection).addAll(update.inputs);
        }
    }

    /** Main game logic. */
    private void tick() {
        // Handle each player's inputs.
        connectionInputs.forEach(this::handleInputs);

        // Move players.
        connectionInputs.keySet().forEach(this::movePlayer);

        // Forget all inputs received since last tick.
        sendPlayerPositionUpdates();
        clearPlayerInputs();
    }

    private void handleInputs(VoConnection c, Set<PlayerAction> actions) {
        actions.forEach(a -> {
            if (a == PlayerAction.MOVE_LEFT) {
                applyForce(-1, 0, c);
            } else if (a == PlayerAction.MOVE_RIGHT) {
                applyForce(1, 0, c);
            } else if (a == PlayerAction.MOVE_UP) {
                applyForce(0, 1, c);
            } else if (a == PlayerAction.MOVE_DOWN) {
                applyForce(0, -1, c);
            }
        });
    }

    /**
     * Given a direction to move, apply a force to the connection's player object.
     * @param xDir The direction to move on the x axis.
     * @param yDir The direction to move on the y axis.
     * @param c    The connection requesting the action.
     */
    private void applyForce(int xDir, int yDir, VoConnection c) {
        Vector2 shift = new Vector2(BASE_MOVEMENT_SPEED * xDir, BASE_MOVEMENT_SPEED * yDir);

        c.player.vel.add(shift);
        c.player.vel.limit(BASE_MOVEMENT_SPEED);
    }

    /**
     * Update player positions given their velocity vectors.
     * @param c The connection whose player object to update.
     */
    private void movePlayer(VoConnection c) {
        // Apply drag.
        applyDrag(c);

        // Move the player.
        float xIncrement = c.player.vel.x;
        float yIncrement = c.player.vel.y;

        c.player.pos.addX(xIncrement);
        c.player.pos.addY(yIncrement);
    }

    private void applyDrag(VoConnection c) {
        c.player.vel.x *= DRAG_CONSTANT;
        c.player.vel.y *= DRAG_CONSTANT;

        // Round off to 0 if already very small.
        if (Math.abs(c.player.vel.x) < 0.1) c.player.vel.x = 0;
        if (Math.abs(c.player.vel.y) < 0.1) c.player.vel.y = 0;
    }

    private void sendPlayerPositionUpdates() {
        List<Player> players = connectionInputs.keySet().stream()
                .map(c -> c.player)
                .collect(Collectors.toList());

        for (VoConnection c : connectionInputs.keySet()) {
            for (Player p : players) {
                c.sendTCP(new PlayerPositionUpdate(p.pos, p.id));
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

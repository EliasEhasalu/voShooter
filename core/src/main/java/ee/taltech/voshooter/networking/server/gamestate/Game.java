package ee.taltech.voshooter.networking.server.gamestate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import ee.taltech.voshooter.networking.messages.serverreceived.PlayerInput;
import ee.taltech.voshooter.networking.server.VoConnection;

public class Game extends Thread {

    private static final double TICK_RATE = 1000000000.0 / 64.0;

    private Map<VoConnection, Optional<PlayerInput>> connectionInputs = new HashMap<>();
    private boolean running = false;

    /**
     * Add a connection to this game.
     * @param connection The connection to add.
     */
    public void addConnection(VoConnection connection) {
        connectionInputs.computeIfAbsent(connection, k -> Optional.empty());
    }

    /**
     * Add an update to be calculated next tick.
     * @param connection The connection the update came from.
     * @param update The update to add.
     */
    public void addUpdate(VoConnection connection, PlayerInput update) {
        connectionInputs.computeIfPresent(connection, (k, v) -> Optional.of(update));
    }

    /** Main game logic. */
    private void tick() {
        for (Map.Entry<VoConnection, Optional<PlayerInput>> pair : connectionInputs.entrySet()) {
            String out = "";
            if (pair.getValue().isPresent()) {
                out += String.format("Lobby: %s", pair.getKey().user.currentLobby);
                out += String.format("- %s sent ", pair.getKey().user.name);
                out += String.format("%s", pair.getValue().get().inputs.toString());
                System.out.println(out);
            }
        }
        clearPlayerInputs();
    }

    /** Reset "last input" for every connection after every tick. */
    private void clearPlayerInputs() {
        connectionInputs.replaceAll((k, v) -> Optional.empty());
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
}

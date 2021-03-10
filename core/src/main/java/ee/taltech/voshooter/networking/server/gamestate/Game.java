package ee.taltech.voshooter.networking.server.gamestate;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ee.taltech.voshooter.networking.server.VoConnection;

public class Game extends Thread {

    private static final double TICK_RATE = 1000000000.0 / 64.0;

    private Set<VoConnection> connections = new HashSet<>();
    private boolean running = false;
    private List<List<Integer>> updates = new LinkedList<>();

    /**
     * Add a connection to this game.
     * @param connection The connection to add.
     */
    public void addConnection(VoConnection connection) {
        if (!connections.contains(connection)) connections.add(connection);
    }

    /**
     * Add an update to be calculated next tick.
     * @param update The update to add.
     */
    public void addUpdate(List<Integer> update) {
        updates.add(update);
    }

    /** Main game logic. */
    private void tick() {
        if (!updates.isEmpty()) System.out.println(updates.get(updates.size() - 1));
        else System.out.println("no input");
        updates.clear();
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

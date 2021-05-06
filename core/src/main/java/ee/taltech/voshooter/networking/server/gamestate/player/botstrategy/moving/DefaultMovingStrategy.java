package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.moving;

import ee.taltech.voshooter.networking.server.gamestate.collision.utils.LevelGenerator;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.Node;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.PathFinding;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.PixelToSimulation;
import ee.taltech.voshooter.networking.server.gamestate.player.Bot;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.BotStrategy;

import java.util.List;
import java.util.Random;

public class DefaultMovingStrategy implements MovingStrategy {

    private static final Random R = new Random();
    private Bot bot;
    private transient int[][] walls;
    private transient BotStrategy parent;

    @Override
    public int[] getMovementDirections(Player closestEnemy, boolean targetIsHitScanned) {
        if (closestEnemy != null) return navigateToTarget(closestEnemy, targetIsHitScanned);
        return new int[] {R.nextInt(3) - 1, R.nextInt(3) - 1};
    }

    @Override
    public void setBot(Bot bot) {
        this.bot = bot;
        this.walls = LevelGenerator.getWallGrid(bot.getGame().getCurrentMap());
    }

    private int[] navigateToTarget(Player enemy, boolean targetIsHitScanned) {
        List<Node> nodes = getPathTo(enemy);
        if (nodes.size() <= 2) return new int[] {0, 0};
        if (targetIsHitScanned && nodes.size() <= 16) return new int[] {0, 0};
        else return nodes.get(1).subtract(nodes.get(0));
    }

    private List<Node> getPathTo(Player enemy) {
        // Min and max calls ensure the start and end nodes remain within the mapped play area
        // (sometimes they are able to escape due to explosions, presumably).
        int[] startArray = PixelToSimulation.castToGrid(bot.getPos());
        Node start = new Node(Math.min(startArray[0], walls[0].length - 1),
                Math.min(startArray[1], walls.length - 1), 0);
        int[] endArray = PixelToSimulation.castToGrid(enemy.getPos());
        Node target = new Node(Math.min(endArray[0], walls[0].length - 1),
                Math.min(endArray[1], walls.length - 1), 0);

        return PathFinding.bfsPath(start, target, walls);
    }
}

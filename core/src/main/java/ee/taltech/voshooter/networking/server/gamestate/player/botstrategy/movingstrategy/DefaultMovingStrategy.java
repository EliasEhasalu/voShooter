package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.movingstrategy;

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
    public int[] getMovementDirections(Player closestEnemy) {
        if (closestEnemy != null) return navigateToTarget(closestEnemy);
        return new int[] {R.nextInt(3) - 1, R.nextInt(3) - 1};
    }

    @Override
    public void setBot(Bot bot) {
        this.bot = bot;
        this.walls = LevelGenerator.getWallGrid(bot.getGame().getCurrentMap());
    }

    private int[] navigateToTarget(Player enemy) {
        List<Node> nodes = getPathTo(enemy);
        if (nodes.size() <= 1) return new int[] {0, 0};
        else return nodes.get(1).subtract(nodes.get(0));
    }

    private List<Node> getPathTo(Player enemy) {
        Node start = new Node(PixelToSimulation.castToGrid(bot.getPos()), 0);
        Node target = new Node(PixelToSimulation.castToGrid(enemy.getPos()), 0);

        return PathFinding.bfsPath(start, target, walls);
    }
}

package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.movingstrategy;

import ee.taltech.voshooter.networking.server.gamestate.collision.utils.LevelGenerator;
import ee.taltech.voshooter.networking.server.gamestate.player.Bot;

import java.util.Random;

public class DefaultMovingStrategy implements MovingStrategy {

    private static final Random R = new Random();
    private Bot bot;
    private transient int[][] walls;

    @Override
    public int[] getMovementDirections() {
        return new int[] {R.nextInt(3) - 1, R.nextInt(3) - 1};
    }

    @Override
    public void setBot(Bot bot) {
        this.bot = bot;
        this.walls = LevelGenerator.getWallGrid(bot.getGame().getCurrentMap());
    }
}

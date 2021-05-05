package ee.taltech.voshooter.networking.server.gamestate.player;

import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.server.gamestate.entitymanager.PlayerManager;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.BalancingBotStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.BotAction;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.BotStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.DefaultBotStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.movingstrategy.DefaultMovingStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.shootingstrategy.DefaultShootingStrategy;

import java.util.Random;

public class Bot extends Player {

    private transient BotStrategy strategy;

    /** Serialize. */
    public Bot() {
    }

    public Bot(PlayerManager playerManager, long id, String name) {
        super(playerManager, null, id, name);
        int random = new Random().nextInt(2);
        if (random == 0) this.strategy = new DefaultBotStrategy(
            this,
            new DefaultShootingStrategy(),
            new DefaultMovingStrategy()
        );
        else this.strategy = new BalancingBotStrategy(
                this,
                new DefaultShootingStrategy(),
                new DefaultMovingStrategy());
        this.bot = true;

        setViewDirection(new MouseCoords(1, 1));
        getInventory().swapToRandomWeapon();
    }

    @Override
    public void update() {
        performAction(strategy.getAction());
        if (getWeapon().getRemainingAmmo() == 0) getInventory().swapToRandomWeapon();
        super.update();
    }

    private void performAction(BotAction action) {
        setViewDirection(action.getAim());
        addMoveDirection(action.getXMoveDir(), action.getYMoveDir());
        if (action.isShooting()) shoot();
    }
}

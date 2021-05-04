package ee.taltech.voshooter.networking.server.gamestate.player;

import ee.taltech.voshooter.networking.server.gamestate.entitymanager.PlayerManager;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.BotAction;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.BotStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.DefaultBotStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.shootingstrategy.DefaultShootingStrategy;

public class Bot extends Player {

    private transient final BotStrategy strategy = new DefaultBotStrategy(this, new DefaultShootingStrategy());

    /** Serialize. */
    public Bot() {
    }

    public Bot(PlayerManager playerManager, long id, String name) {
        super(playerManager, null, id, name);
        bot = true;
    }

    @Override
    public void update() {
        performAction(strategy.getAction());
        super.update();
    }

    private void performAction(BotAction action) {
        setViewDirection(action.getAim());
        if (action.isShooting()) shoot();
    }
}

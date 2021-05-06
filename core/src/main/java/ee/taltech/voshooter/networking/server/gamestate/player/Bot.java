package ee.taltech.voshooter.networking.server.gamestate.player;

import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.server.gamestate.entitymanager.PlayerManager;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.BotAction;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.BotStrategy;

public class Bot extends Player {

    private transient BotStrategy strategy;

    /** Serialize. */
    public Bot() {
    }

    public Bot(PlayerManager playerManager, long id, String name, BotStrategy botStrategy) {
        super(playerManager, null, id, name);
        this.strategy = botStrategy;
        this.bot = true;
        this.strategy.setBot(this);

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

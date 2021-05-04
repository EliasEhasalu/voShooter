package ee.taltech.voshooter.networking.server.gamestate.player;

import ee.taltech.voshooter.networking.server.gamestate.entitymanager.PlayerManager;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.BotAction;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.BotStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.DefaultBotStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.movingstrategy.DefaultMovingStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.shootingstrategy.DefaultShootingStrategy;
import ee.taltech.voshooter.weapon.Weapon;

public class Bot extends Player {

    private transient final BotStrategy strategy = new DefaultBotStrategy(
            this,
            new DefaultShootingStrategy(),
            new DefaultMovingStrategy()
    );

    /** Serialize **/
    public Bot() {
    }

    public Bot(PlayerManager playerManager, long id, String name) {
        super(playerManager, null, id, name);
        bot = true;
        getInventory().swapToWeapon(Weapon.Type.RAILGUN);
    }

    @Override
    public void update() {
        performAction(strategy.getAction());
        super.update();
    }

    private void performAction(BotAction action) {
        setViewDirection(action.getAim());
        addMoveDirection(action.getXMoveDir(), action.getYMoveDir());
        if (action.isShooting()) shoot();
    }
}

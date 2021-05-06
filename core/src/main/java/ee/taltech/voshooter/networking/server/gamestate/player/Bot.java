package ee.taltech.voshooter.networking.server.gamestate.player;

import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.server.gamestate.entitymanager.PlayerManager;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.BotAction;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.BotStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.DefaultBotStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.moving.DefaultMovingStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.shooting.DefaultShootingStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.weaponswitching.DefaultWeaponSwitchingStrategy;

public class Bot extends Player {

    private transient BotStrategy strategy;

    /** Serialize. */
    public Bot() {
    }

    public Bot(PlayerManager playerManager, long id, String name) {
        super(playerManager, null, id, name);
        this.strategy = new DefaultBotStrategy(
            this,
            new DefaultShootingStrategy(),
            new DefaultMovingStrategy(),
            new DefaultWeaponSwitchingStrategy()
        );
        this.bot = true;

        setViewDirection(new MouseCoords(1, 1));
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
        if (action.getWeaponToSwitchTo() != null) getInventory().swapToWeapon(action.getWeaponToSwitchTo());
    }
}

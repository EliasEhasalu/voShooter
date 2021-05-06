package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy;

import ee.taltech.voshooter.networking.server.gamestate.player.Bot;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.movingstrategy.MovingStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.shootingstrategy.ShootingStrategy;

public class BalancingBotStrategy extends DefaultBotStrategy implements BotStrategy {

    public BalancingBotStrategy(Bot bot, ShootingStrategy shootingStrategy, MovingStrategy movingStrategy) {
        super(bot, shootingStrategy, movingStrategy);
    }

    public BalancingBotStrategy(ShootingStrategy shootingStrategy, MovingStrategy movingStrategy) {
        super(shootingStrategy, movingStrategy);
    }

    @Override
    public BotAction getAction() {
        BotAction action = new BotAction();
        Player topEnemy = determineTopEnemy();
        targetedBody = getHitScannedTarget();

        action.setAim(determineAimDirection(topEnemy));
        action.setShooting(shootingStrategy.toShoot(targetIsHitScanned()));
        action.setMovementDirections(movingStrategy.getMovementDirections(topEnemy, targetIsHitScanned()));

        return action;
    }

    private Player determineTopEnemy() {
        return playerManager.getTopPlayer();
    }
}

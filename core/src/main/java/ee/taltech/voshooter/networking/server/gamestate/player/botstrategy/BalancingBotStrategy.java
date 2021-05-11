package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.moving.MovingStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.shooting.ShootingStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.weaponswitching.WeaponSwitchingStrategy;

import java.util.Optional;

public class BalancingBotStrategy extends DefaultBotStrategy implements BotStrategy {

    public BalancingBotStrategy(ShootingStrategy shootingStrategy, MovingStrategy movingStrategy, WeaponSwitchingStrategy weaponSwitchingStrategy) {
        super(shootingStrategy, movingStrategy, weaponSwitchingStrategy);
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
        Optional<Player> p = playerManager.getTopPlayer().stream().filter(player -> !player.equals(bot)).findFirst();
        return p.orElse(null);
    }
}

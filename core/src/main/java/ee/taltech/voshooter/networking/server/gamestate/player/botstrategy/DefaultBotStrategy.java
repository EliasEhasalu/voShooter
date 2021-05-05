package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.RayCaster;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.RayCollision;
import ee.taltech.voshooter.networking.server.gamestate.entitymanager.PlayerManager;
import ee.taltech.voshooter.networking.server.gamestate.player.Bot;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.movingstrategy.MovingStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.shootingstrategy.ShootingStrategy;

import java.util.HashSet;
import java.util.Set;

public class DefaultBotStrategy implements BotStrategy {

    private static final RayCaster rayCaster = new RayCaster();
    private final float turningSpeed = 1440;

    private final Bot bot;
    private final PlayerManager playerManager;
    private final ShootingStrategy shootingStrategy;
    private final MovingStrategy movingStrategy;

    private Body targetedBody;

    public DefaultBotStrategy(Bot bot, ShootingStrategy shootingStrategy, MovingStrategy movingStrategy) {
        this.bot = bot;
        this.playerManager = bot.getPlayerManager();

        this.shootingStrategy = shootingStrategy; shootingStrategy.setBot(bot);
        this.movingStrategy = movingStrategy; movingStrategy.setBot(bot);
    }

    @Override
    public BotAction getAction() {
        BotAction action = new BotAction();
        Player closestEnemy = determineClosestEnemy();
        targetedBody = getHitScannedTarget();

        action.setAim(determineAimDirection(closestEnemy));
        action.setShooting(shootingStrategy.toShoot(targetIsHitScanned()));
        action.setMovementDirections(movingStrategy.getMovementDirections(closestEnemy, targetIsHitScanned()));

        return action;
    }

    private MouseCoords determineAimDirection(Player closestEnemy) {
        if (closestEnemy == null) return new MouseCoords(bot.getViewDirection().x, bot.getViewDirection().y);
        else return lookTowardsTargetMouseCoords(closestEnemy);
    }

    private MouseCoords lookTowardsTargetMouseCoords(Player target) {
        Vector2 directionToTarget = target.getPos().cpy().sub(bot.getPos().cpy());
        Vector2 currentViewDirection = bot.getViewDirection().cpy();
        float angleDiff = directionToTarget.angleDeg() - currentViewDirection.angleDeg();
        float turnBy = turningSpeed * Game.timeElapsed();
        float actualTurn = (Math.abs(angleDiff) < Math.abs(turnBy)) ? angleDiff : turnBy;
        if (angleDiff > 180) actualTurn = -actualTurn;

        Vector2 newViewDirection = currentViewDirection.cpy().rotateDeg(actualTurn);
        return new MouseCoords(newViewDirection.x, newViewDirection.y);
    }

    private Player determineClosestEnemy() {
        Player closest = null;
        float closestDist = Float.MAX_VALUE;

        for (Player p : getPlayers()) {
            if (p != bot && p.isAlive() && getDistanceTo(p) < closestDist) {
                closest = p;
                closestDist = getDistanceTo(p);
            }
        }

        return closest;
    }


    private Body getHitScannedTarget() {
        RayCollision collision = rayCaster.getFirstCollision(
                bot.getGame(),
                bot.getPos(),
                bot.getViewDirection(),
                50f,
                new HashSet<>()
        );

        Body b = null;
        if (collision != null) b = collision.getCollidedBody();

        return b;
    }

    private boolean targetIsHitScanned() {
        return (targetedBody != null && targetedBody.getUserData() instanceof Player);
    }

    private float getDistanceTo(Player p) {
        return Vector2.dst(bot.getPos().x, bot.getPos().y, p.getPos().x, p.getPos().y);
    }

    private Set<Player> getPlayers() {
        return playerManager.getPlayers();
    }
}

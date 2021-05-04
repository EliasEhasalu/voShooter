package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.server.gamestate.entitymanager.PlayerManager;
import ee.taltech.voshooter.networking.server.gamestate.player.Bot;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;

import java.util.Set;

public class DefaultBotStrategy implements BotStrategy {

    private final Bot bot;
    private final PlayerManager playerManager;

    public DefaultBotStrategy(Bot bot) {
        this.bot = bot;
        this.playerManager = bot.getPlayerManager();
    }

    @Override
    public BotAction getAction() {
        BotAction action = new BotAction();
        action.setAim(determineAimDirection());

        return action;
    }

    private MouseCoords determineAimDirection() {
        Player closest = determineClosestEnemy();
        if (closest == null) return new MouseCoords(bot.getViewDirection().x, bot.getViewDirection().y);
        else {
            Vector2 viewDirection = closest.getPos().cpy().sub(bot.getPos().cpy());
            return new MouseCoords(viewDirection.x, viewDirection.y);
        }
    }

    private Player determineClosestEnemy() {
        Player closest = null;
        float closestDist = Float.MAX_VALUE;

        for (Player p : getPlayers()) {
            if (p != bot && getDistanceTo(p) < closestDist) {
                closest = p;
                closestDist = getDistanceTo(p);
            }
        }

        return closest;
    }

    private float getDistanceTo(Player p) {
        return Vector2.dst(bot.getPos().x, bot.getPos().y, p.getPos().x, p.getPos().y);
    }

    private Set<Player> getPlayers() {
        return playerManager.getPlayers();
    }
}

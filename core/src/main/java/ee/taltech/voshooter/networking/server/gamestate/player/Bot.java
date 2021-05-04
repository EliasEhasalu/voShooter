package ee.taltech.voshooter.networking.server.gamestate.player;

import ee.taltech.voshooter.networking.server.gamestate.entitymanager.PlayerManager;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.BotStrategy;
import ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.DefaultBotStrategy;

public class Bot extends Player {

    private final BotStrategy strategy = new DefaultBotStrategy();

    public Bot(PlayerManager playerManager, long id, String name) {
        super(playerManager, null, id, name);
    }
}

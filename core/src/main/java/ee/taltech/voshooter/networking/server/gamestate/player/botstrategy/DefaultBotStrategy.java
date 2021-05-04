package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy;

import ee.taltech.voshooter.networking.server.gamestate.player.Bot;

public class DefaultBotStrategy implements BotStrategy {

    private final Bot bot;

    public DefaultBotStrategy(Bot bot) {
        this.bot = bot;
    }

    @Override
    public BotAction getAction() {
        return new BotAction();
    }
}

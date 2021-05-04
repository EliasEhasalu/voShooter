package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy;

public class DefaultBotStrategy implements BotStrategy {

    @Override
    public BotAction getAction() {
        return new BotAction();
    }
}

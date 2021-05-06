package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy;

import ee.taltech.voshooter.networking.server.gamestate.player.Bot;

public interface BotStrategy {

    BotAction getAction();

    void setBot(Bot bot);
}

package ee.taltech.voshooter.networking.messages.clientreceived;

import ee.taltech.voshooter.networking.messages.Player;

import java.util.List;

public class GameStarted {

    public List<Player> players;

    public GameStarted() {
    }

    public GameStarted(List<Player> players) {
        this.players = players;
    }
}

package ee.taltech.voshooter.networking.messages.clientreceived;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.messages.User;

import java.util.List;

public class LobbyUserUpdate {

    public List<User> users;
    public List<Player> players;

    /** Serialize. */
    public LobbyUserUpdate() {
    }

    /**
     * @param users The list of users now in this lobby.
     */
    public LobbyUserUpdate(List<User> users) {
        this.users = users;
    }

    /**
     * @param users The list of users now in this lobby.
     * @param players The list of players now in this lobby.
     */
    public LobbyUserUpdate(List<User> users, List<Player> players) {
        this.users = users;
        this.players = players;
    }
}

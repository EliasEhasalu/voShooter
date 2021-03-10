package ee.taltech.voshooter.networking.messages.clientreceived;

import java.util.List;

import ee.taltech.voshooter.networking.messages.User;

public class LobbyUserUpdate {

    public List<User> users;

    /** Serialize. */
    public LobbyUserUpdate() {
    }

    /**
     * @param users The list of users now in this lobby.
     */
    public LobbyUserUpdate(List<User> users) {
        this.users = users;
    }
}

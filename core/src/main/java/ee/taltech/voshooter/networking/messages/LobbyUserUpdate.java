package ee.taltech.voshooter.networking.messages;

import java.util.List;

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

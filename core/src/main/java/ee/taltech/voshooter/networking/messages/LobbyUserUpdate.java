package ee.taltech.voshooter.networking.messages;

import java.util.ArrayList;
import java.util.List;

public class LobbyUserUpdate {

    public List<User> users = new ArrayList<>();

    /** Default constructor for serialization. */
    public LobbyUserUpdate() {
    }

    /**
     * @param users The list of users in this lobby.
     */
    public LobbyUserUpdate(List<User> users) {
        this.users = users;
    }
}

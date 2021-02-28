package ee.taltech.voshooter.networking.server.lobby;

import java.util.ArrayList;
import java.util.List;

import ee.taltech.voshooter.networking.server.User;

public class Lobby {

    private List<User> users = new ArrayList<>();

    /**
     * Add a user to this lobby.
     * @param user The user to add.
     */
    public void addUser(User user) {
        if (!users.contains(user)) users.add(user);
    }
}

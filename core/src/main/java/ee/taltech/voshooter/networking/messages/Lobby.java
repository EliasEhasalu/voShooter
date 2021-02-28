package ee.taltech.voshooter.networking.messages;

import java.util.ArrayList;
import java.util.List;

import ee.taltech.voshooter.networking.server.VoServer.User;


public class Lobby {

    private List<User> users = new ArrayList<>();

    /**
     * Add a user to this lobby.
     * @param user The user to add.
     */
    public void addUser(User user) {
        if (!users.contains(user)) users.add(user);
    }

    /**
     * @return Whether this lobby contains the given user.
     * @param user The given user.
     */
    public boolean containsUser(User user) {
        return (users.contains(user));
    }

    /** @return  The amount of users in this lobby.*/
    public int getUserCount() {
        return users.size();
    }
}

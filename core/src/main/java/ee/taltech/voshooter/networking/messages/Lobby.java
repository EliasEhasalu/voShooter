package ee.taltech.voshooter.networking.messages;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private int maxUsers;

    private List<User> users = new ArrayList<>();
    private String lobbyCode;

    /** Default constructor for serialization. */
    public Lobby() {
    }

    /**
     * @param maxUsers The maximum amount of users that can be in this lobby.
     * @param lobbyCode The lobby code assigned to this lobby.
     */
    public Lobby(int maxUsers, String lobbyCode) {
        this.lobbyCode = lobbyCode;
        this.maxUsers = maxUsers;
    }

    /**
     * Add a user to this lobby.
     * @param user The user to add.
     * @return Whether the adding was successful.
     */
    public boolean addUser(User user) {
        if (!users.contains(user) && users.size() < maxUsers) {
            users.add(user);
            return true;
        }
        return false;
    }

    /**
     * Remove a user from this lobby.
     * @param user The user to remove.
     */
    public void removeUser(User user) {
        if (users.contains(user)) users.remove(user);
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

    /**
     * @return The list of users in this lobby.
     */
    public List<User> getUsers() {
        return users;
    }

    /** @return The code for this lobby. */
    public String getLobbyCode() {
        return lobbyCode;
    }
}

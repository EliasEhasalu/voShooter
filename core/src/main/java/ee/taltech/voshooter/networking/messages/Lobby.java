package ee.taltech.voshooter.networking.messages;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private List<User> users = new ArrayList<>();
    private String lobbyCode = "";

    /**
     * Default constructor for serialization.
     */
    public Lobby() {
    }

    /**
     * Add a user to this lobby.
     * @param user The user to add.
     */
    public void addUser(User user) {
        if (!users.contains(user)) users.add(user);
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

    /** @param code The code to set this lobby's code to. */
    public void setLobbyCode(String code) {
        lobbyCode = code;
    }
}

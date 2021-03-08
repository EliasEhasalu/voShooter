package ee.taltech.voshooter.networking.server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ee.taltech.voshooter.networking.messages.LobbyUserUpdate;
import ee.taltech.voshooter.networking.messages.User;
import ee.taltech.voshooter.networking.server.VoServer.Remote;

public class Lobby {

    private int maxUsers;
    private int gameMode;
    private String lobbyCode;
    private Remote host;

    private List<Remote> users = new ArrayList<>();

    /** Default constructor for serialization. */
    public Lobby() {
    }

    /**
     * @param maxUsers The maximum amount of users that can be in this lobby.
     * @param gameMode An integer representing the gamemode of this lobby.
     * @param lobbyCode The lobby code assigned to this lobby.
     */
    public Lobby(int maxUsers, int gameMode, String lobbyCode) {
        this.maxUsers = maxUsers;
        this.gameMode = gameMode;
        this.lobbyCode = lobbyCode;
    }

    /**
     * Add a user to this lobby.
     * @param user The user to add.
     * @return Whether the adding was successful.
     */
    public boolean addUser(Remote user) {
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
    public void removeUser(Remote user) {
        if (users.contains(user)) {
            users.remove(user);

            // Construct the update message.
            LobbyUserUpdate update = new LobbyUserUpdate();
            for (Remote u : users) {
                update.users.add(u.getUser());
            }
            // Send the update message to all users in this lobby.
            for (Remote u : users) {
                u.client.updateLobbyUsers(update);
            }
        }
    }

    /**
     * @return Whether this lobby contains the given user.
     * @param user The given user.
     */
    public boolean containsUser(Remote user) {
        return (users.contains(user));
    }

    /** @return  The amount of users in this lobby.*/
    public int getUserCount() {
        return users.size();
    }

    /** @return This lobby's gamemode. */
    public int getGameMode() {
        return gameMode;
    }

    /**
     * @return This lobby's max users.
     */
    public int getMaxUsers() {
        return maxUsers;
    }

    /**
     * @return The list of users in this lobby.
     */
    public List<User> getUsers() {
        return users.stream()
            .map(Remote::getUser)
            .collect(Collectors.toList());
    }

    /** @return The code for this lobby. */
    public String getLobbyCode() {
        return lobbyCode;
    }

    /**
     * @param user The user to set as the host of this lobby.
     */
    public void setHost(Remote user) {
        this.host = user;
    }

    /** @return This lobby's host. */
    public Remote getHost() {
        return host;
    }
}

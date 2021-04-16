package ee.taltech.voshooter.gamestate;

import ee.taltech.voshooter.map.GameMap;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.messages.User;
import ee.taltech.voshooter.networking.messages.clientreceived.LobbyJoined;

import java.util.ArrayList;
import java.util.List;

public class ClientLobby {

    private int maxUsers = 4;
    private int gamemode = 0;
    private List<User> users = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private String lobbyCode;
    private GameMap.MapType mapType;

    private GameState parent;

    /**
     * Instantiate client lobby object.
     * @param parent The game state parent of this object.
     */
    public ClientLobby(GameState parent) {
        this.parent = parent;
    }

    /**
     * @return Amount of users in the lobby.
     */
    public int getUsersCount() {
        return users.size();
    }

    /**
     * Add a user to the lobby if there is enough space.
     * @param user The user to add.
     * @return If user was added successfully.
     */
    public boolean addUser(User user) {
        if (!users.contains(user) && users.size() < maxUsers) {
            users.add(user);
            return true;
        }
        return false;
    }

    /**
     * Remove user from the lobby.
     * @param user The user to remove.
     * @return If user was removed successfully.
     */
    public boolean removeUser(User user) {
        if (users.contains(user)) {
            users.remove(user);
            players.removeIf(player -> player.getId() == user.id);
            return true;
        }
        return false;
    }

    /**
     * @param user The user to check in the lobby.
     * @return Whether the user is in the lobby or not.
     */
    public boolean containsUser(User user) {
        return users.contains(user);
    }

    /**
     * Clear the current lobby.
     */
    public void clearLobby() {
        users.clear();
        players.clear();
        lobbyCode = null;
        maxUsers = 4;
        gamemode = 0;
        mapType = null;
    }

    /**
     * @return Maximum number of users in lobby.
     */
    public int getMaxUsers() {
        return maxUsers;
    }

    /**
     * @return Gamemode of the lobby.
     */
    public int getGamemode() {
        return gamemode;
    }

    /**
     * @return List of users in lobby.
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * @return List of users in lobby.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @return The code of the lobby.
     */
    public String getLobbyCode() {
        return lobbyCode;
    }

    /** @return The game map used in the lobby. */
    public GameMap.MapType getMap() {
        return mapType;
    }

    /**
     * @param users Set the list of users.
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * @param players Set the list of players.
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * @param gamemode Set the lobby gamemode.
     */
    public void setGamemode(int gamemode) {
        this.gamemode = gamemode;
    }

    /**
     * @param lobbyCode Set the code for the lobby.
     */
    public void setLobbyCode(String lobbyCode) {
        this.lobbyCode = lobbyCode;
    }

    /**
     * @param maxUsers Set the max amount of users for the lobby.
     */
    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    /**
     * Set the game map for the lobby.
     * @param mapType The map used.
     */
    public void setMapType(GameMap.MapType mapType) {
        this.mapType = mapType;
    }

    /**
     * Set initial parameters upon joining lobby.
     * @param message The message with lobby info received from the server.
     */
    public void handleJoining(LobbyJoined message) {
        parent.clientUser.id = message.id;
        if (message.host.id == parent.clientUser.id) {
            parent.clientUser.setHost(true);
        }
        setGamemode(message.gameMode);
        setMaxUsers(message.maxPlayers);
        addUser(parent.clientUser);
        setLobbyCode(message.lobbyCode);
        setUsers(message.users);
        this.mapType = message.mapType;
    }
}

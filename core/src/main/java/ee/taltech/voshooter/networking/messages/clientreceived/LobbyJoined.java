package ee.taltech.voshooter.networking.messages.clientreceived;

import java.util.List;

import ee.taltech.voshooter.networking.messages.User;

/**
 * This class specifies the state of the lobby at the time of entering it.
 */
public class LobbyJoined {

    public int gameMode;
    public int maxPlayers;
    public String lobbyCode;
    public List<User> users;
    public User host;
    public long id;

    /** For serialization. */
    public LobbyJoined() {
    }

    /**
     * @param gameMode The gamemode of the created lobby.
     * @param maxPlayers The maximum amount of players in the created lobby.
     * @param lobbyCode The lobby code of the lobby created.
     * @param users List of users in this lobby.
     * @param host The host of the lobby.
     * @param id The ID of the client user.
     */
    public LobbyJoined(int gameMode, int maxPlayers, String lobbyCode, List<User> users, User host, long id) {
        this.gameMode = gameMode;
        this.maxPlayers = maxPlayers;
        this.lobbyCode = lobbyCode;
        this.users = users;
        this.host = host;
        this.id = id;
    }
}

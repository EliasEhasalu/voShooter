package ee.taltech.voshooter.networking.messages;

import java.util.List;

/**
 * This class specifies the state of the lobby at the time of entering it.
 */
public class LobbyEntry {

    public int gameMode;
    public int maxPlayers;
    public String lobbyCode;
    public List<User> users;
    public User host;

    /** For serialization. */
    public LobbyEntry() {
    }

    /**
     * @param gameMode The gamemode of the created lobby.
     * @param maxPlayers The maximum amount of players in the created lobby.
     * @param lobbyCode The lobby code of the lobby created.
     * @param users List of users in this lobby.
     * @param host The host of the lobby.
     */
    public LobbyEntry(int gameMode, int maxPlayers, String lobbyCode, List<User> users, User host) {
        this.gameMode = gameMode;
        this.maxPlayers = maxPlayers;
        this.lobbyCode = lobbyCode;
        this.users = users;
        this.host = host;
    }
}

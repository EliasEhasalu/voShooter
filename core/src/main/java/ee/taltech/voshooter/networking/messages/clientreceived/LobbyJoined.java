package ee.taltech.voshooter.networking.messages.clientreceived;

import ee.taltech.voshooter.map.GameMap;
import ee.taltech.voshooter.networking.messages.User;

import java.util.List;

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
    public GameMap.MapType mapType;

    /** For serialization. */
    public LobbyJoined() {
    }

    /**
     * @param gameMode The game mode of the created lobby.
     * @param maxPlayers The maximum amount of players in the created lobby.
     * @param lobbyCode The lobby code of the lobby created.
     * @param users List of users in this lobby.
     * @param host The host of the lobby.
     * @param id The ID of the client user.
     * @param mapType The game map used in this lobby.
     */
    public LobbyJoined(int gameMode, int maxPlayers, String lobbyCode, List<User> users,
                       User host, long id, GameMap.MapType mapType) {
        this.gameMode = gameMode;
        this.maxPlayers = maxPlayers;
        this.lobbyCode = lobbyCode;
        this.users = users;
        this.host = host;
        this.id = id;
        this.mapType = mapType;
    }
}

package ee.taltech.voshooter.networking.messages;

public class LobbyCreated {

    public int maxPlayers;
    public int gameMode;
    public String lobbyCode;

    /** Default constructor for serialisation. */
    public LobbyCreated() {
        //.
    }

    /**
     * @param maxPlayers Maximum amount of players in lobby.
     * @param gameMode Gamemode of the match.
     * @param lobbyCode Code to join the lobby.
     */
    public LobbyCreated(int maxPlayers, int gameMode, String lobbyCode) {
        this.maxPlayers = maxPlayers;
        this.gameMode = gameMode;
        this.lobbyCode = lobbyCode;
    }
}

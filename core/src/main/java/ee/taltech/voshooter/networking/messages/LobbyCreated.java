package ee.taltech.voshooter.networking.messages;

public class LobbyCreated {

    public int maxPlayers;
    public int gameMode;
    public String lobbyCode;

    /** Default constructor for serialization. */
    public LobbyCreated() {
    }

    /**
     * @param maxPlayers Max amount of players in the created lobby.
     * @param gameMode The game mode of the lobby.
     * @param lobbyCode The generated lobby code of the lobby.
     */
    public LobbyCreated(int maxPlayers, int gameMode, String lobbyCode) {
        this.maxPlayers = maxPlayers;
        this.gameMode = gameMode;
        this.lobbyCode = lobbyCode;
    }
}

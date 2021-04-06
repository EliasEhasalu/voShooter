package ee.taltech.voshooter.networking.messages.serverreceived;

public class CreateLobby {

    public int gameMode;
    public int maxPlayers;

    /** Default constructor for serialization. */
    public CreateLobby() {
    }

    /**
     * @param gameMode The game mode of the lobby to be created.
     * @param maxPlayers The maximum amount of players the newly created lobby can hold.
     */
    public CreateLobby(int gameMode, int maxPlayers) {
        this.gameMode = gameMode;
        this.maxPlayers = maxPlayers;
    }
}

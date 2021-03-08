package ee.taltech.voshooter.networking;

public interface RemoteInterface {

    /**
     * Set this user's name.
     * @param name The name to set the user's name to.
     */
    void setUserName(String name);

    /**
     * Create a lobby.
     * @param numberOfPlayers Max number of players that can join the lobby.
     * @param gameMode Gamemode of the match.
     */
    void createLobby(int numberOfPlayers, int gameMode);
}

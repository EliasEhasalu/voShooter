package ee.taltech.voshooter.networking;

import ee.taltech.voshooter.networking.messages.LobbyCreated;
import ee.taltech.voshooter.networking.messages.LobbyJoined;

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
     * @return A message object containing the lobby code string.
     */
    LobbyCreated createLobby(int numberOfPlayers, int gameMode);

    /**
     * Join a lobby.
     * @param lobbyCode of the lobby
     * @return A message containing user list and boolean of successful join
     */
    LobbyJoined joinLobby(String lobbyCode);
}

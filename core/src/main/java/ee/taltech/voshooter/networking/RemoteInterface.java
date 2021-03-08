package ee.taltech.voshooter.networking;

import ee.taltech.voshooter.networking.messages.Lobby;

public interface RemoteInterface {

    /**
     * Create a lobby.
     * @param numberOfPlayers Max number of players that can join the lobby.
     * @param gameMode Gamemode of the match.
     * @return The created lobby.
     */
    Lobby createLobby(int numberOfPlayers, int gameMode);
}

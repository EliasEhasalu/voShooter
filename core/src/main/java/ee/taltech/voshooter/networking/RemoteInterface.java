package ee.taltech.voshooter.networking;

import java.util.List;

import ee.taltech.voshooter.networking.messages.Lobby;

public interface RemoteInterface {
    /**
     * @return Amount of times pinged.
     */
    int ping();

    /**
     * @return A list of avaialable lobbies.
     */
    List<Lobby> getLobbies();

    /**
     * Create a lobby.
     * @return The created lobby.
     */
    Lobby createLobby();
}

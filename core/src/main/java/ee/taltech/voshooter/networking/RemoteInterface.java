package ee.taltech.voshooter.networking;

import ee.taltech.voshooter.networking.messages.Lobby;

public interface RemoteInterface {

    /**
     * Create a lobby.
     * @return The created lobby.
     */
    Lobby createLobby();
}

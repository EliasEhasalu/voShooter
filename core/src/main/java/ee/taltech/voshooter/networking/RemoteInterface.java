package ee.taltech.voshooter.networking;

import java.util.List;
import java.util.Optional;

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

    /**
     * Attempt to join a lobby.
     * @param code of the lobby
     * @return optional of lobby
     */
    Optional<Lobby> joinLobby(String code);
}

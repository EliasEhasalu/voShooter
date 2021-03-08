package ee.taltech.voshooter.networking;

import java.util.List;
import java.util.Optional;
import ee.taltech.voshooter.networking.messages.Lobby;

public interface RemoteInterface {

    /**
     * Create a lobby.
     * @param numberOfPlayers Max number of players that can join the lobby.
     * @param gameMode Gamemode of the match.
     * @return The created lobby.
     */
<<<<<<< core/src/main/java/ee/taltech/voshooter/networking/RemoteInterface.java
    Lobby createLobby();

    /**
     * Attempt to join a lobby.
     * @param code of the lobby
     * @return optional of lobby
     */
    Optional<Lobby> joinLobby(String code);
=======
    Lobby createLobby(int numberOfPlayers, int gameMode);
>>>>>>> core/src/main/java/ee/taltech/voshooter/networking/RemoteInterface.java
}

package ee.taltech.voshooter.networking;

import ee.taltech.voshooter.networking.messages.LobbyUserUpdate;

/**
 * This interface will need to be implemented by VoClient.
 * It will ensure that server requests are handled properly on the client.
 */
public interface ClientInterface {

    /**
     * A user has either joined or left the lobby you're currently in.
     * Update the lobby screen accordingly using the provided LobbyUserUpdate
     * object, which contains a list of User objects that are now in the lobby.
     * @param update A list of user objects currently in the lobby.
     */
    void updateLobbyUsers(LobbyUserUpdate update);
}

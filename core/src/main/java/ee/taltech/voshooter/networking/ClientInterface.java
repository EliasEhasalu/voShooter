package ee.taltech.voshooter.networking;

import ee.taltech.voshooter.networking.messages.User;

/**
 * This interface will need to be implemented by VoClient.
 * It will ensure that server requests are handled properly on the client.
 */
public interface ClientInterface {

    /**
     * A new user has joined the lobby you're currently in.
     * Update the lobby screen accordingly using the provided User object.
     * @param user The user that joined the lobby.
     */
    void userJoinedLobby(User user);

    /**
     * A user has left the lobby you're currently in.
     * Update the lobby screen accordingly using the provided User object.
     * @param user The user that left the lobby.
     */
    void userLeftLobby(User user);
}

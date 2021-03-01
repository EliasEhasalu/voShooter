package ee.taltech.voshooter.networking;

import ee.taltech.voshooter.networking.messages.User;

/**
 * This interface will need to be implemented by VoClient.
 * It will ensure that server requests are handled properly on the client.
 */
public interface ClientInterface {

    /**
     * Signal that a user left.
     * @param user The user that left.
     */
   void userLeft(User user);
}

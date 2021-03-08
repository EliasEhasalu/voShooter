package ee.taltech.voshooter.networking;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

import ee.taltech.voshooter.networking.messages.LobbyUserUpdate;
import ee.taltech.voshooter.networking.messages.User;


public class VoClient {

    public RemoteInterface remote;
    public User clientUser = new User();
    public boolean connectionEstablished = false;

    public Client client;
    ServerEntry serverEntry;

    private static final String HOST_ADDRESS = "localhost";
    private static final int MILLISECONDS_BEFORE_TIMEOUT = 5000;

    /**
     * Construct the client.
     */
    public VoClient() {
        client = new Client();
        client.start();

        // Agree on what messages should be passed.
        Network.register(client);

        // Get the remote object on the server that we can call methods on.
        remote = ObjectSpace.getRemoteObject(client, Network.REMOTE, RemoteInterface.class);

        // Create a ClientInterface object, so the server can call methods on it.
        serverEntry = new ServerEntry();
        new ObjectSpace(client).register(Network.SERVER_ENTRY, serverEntry);

        new Thread("Connect") {
            @Override
            public void run() {
                try {
                    client.connect(MILLISECONDS_BEFORE_TIMEOUT, HOST_ADDRESS, Network.PORT);
                    connectionEstablished = true;
                } catch (IOException ex) {
                    client.close();
                    displayNoConnectionMessage();
                }
            }
        }.start();
    }

    /** Display a message when unable to connect to the server. */
    private void displayNoConnectionMessage() {
        connectionEstablished = false;
        System.out.println("No connection!");
    }

    private static class ServerEntry implements ClientInterface {
        /**
         * Construct the server entry.
         */
        ServerEntry() {
        }

        /**
         * Placeholder.
         * @param update A list of user objects currently in the lobby.
         */
        @Override
        public void updateLobbyUsers(LobbyUserUpdate update) {
        }
    }
}

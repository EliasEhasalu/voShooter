package ee.taltech.voshooter.networking;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;


public class VoClient {

    private static final String HOST_ADDRESS = "localhost";
    private static final int MILLISECONDS_BEFORE_TIMEOUT = 5000;

    Client client;
    public UserComms user;


    /**
     * Construct the client.
     */
    public VoClient() {
        client = new Client();
        client.start();

        // Agree on what messages should be passed.
        Network.register(client);

        // Get the remote object on the server that we can call methods on.
        user = ObjectSpace.getRemoteObject(client, Network.USER, UserComms.class);

        new Thread("Connect") {
            @Override
            public void run() {
                try {
                    client.connect(MILLISECONDS_BEFORE_TIMEOUT, HOST_ADDRESS, Network.PORT);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        }.start();
    }
}

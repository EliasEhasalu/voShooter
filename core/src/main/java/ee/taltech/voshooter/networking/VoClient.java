package ee.taltech.voshooter.networking;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import ee.taltech.voshooter.networking.Network.Hello;

public class VoClient implements ClientInterface {

    private static final String HOST_ADDRESS = "localhost";

    Client client;
    String name;


    /**
     * Construct the client.
     */
    public VoClient() {
        client = new Client();
        client.start();

        // Agree on what messages should be passed.
        Network.register(client);

        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
            }

            @Override
            public void received(Connection c, Object object) {

                if (object instanceof Hello) {
                    System.out.println(((Hello) object).greeting);
                }
            }
        });

        try {
            client.connect(5000, HOST_ADDRESS, Network.PORT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Send a greeting.
     * @param content The content of the greeting.
     */
    public void sendGreeting(String content) {
        Hello hello = new Hello();
        hello.greeting = content;
        client.sendTCP(hello);
    }
}

package ee.taltech.voshooter.networking.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import ee.taltech.voshooter.networking.Network;
import ee.taltech.voshooter.networking.Network.CreateLobby;
import ee.taltech.voshooter.networking.Network.Hello;
import ee.taltech.voshooter.networking.server.lobby.Lobby;

public class VoServer {

    Server server;

    private int timesPinged = 1;
    private List<Lobby> lobbies = new ArrayList<>();

    /**
     * Bootstrap the server upon instantiation.
     */
    public VoServer() throws IOException {
        server = new Server() {
            @Override
            protected Connection newConnection() {
                return new VoConnection();
            }
        };

        // Register classes that will be passed over the connection.
        Network.register(server);

        server.addListener(new Listener() {
            @Override
            public void received(Connection c, Object object) {
                VoConnection connection = (VoConnection) c;

                if (object instanceof Hello) {
                    System.out.println(String.format("%d - %s", timesPinged, ((Hello) object).greeting));
                    Hello reply = new Hello();
                    reply.greeting = String.format("you've pinged %d times", timesPinged++);
                    connection.sendTCP(reply);
                }

                if (object instanceof CreateLobby) {
                    Lobby newLobby = createNewLobby();
                    newLobby.addUser(connection.user);
                }
            }
        });

        server.bind(Network.PORT);
        server.start();
    }

    /**
     * Create and return a new lobby.
     * @return The newly created lobby object.
     */
    private Lobby createNewLobby() {
        Lobby lobby = new Lobby();
        lobbies.add(lobby);
        return lobby;
    }

    /**
     * Main entry point for server.
     * @param args CLI args.
     */
    public static void main(String[] args) throws IOException {
        // Server is launched upon object instantiation.
        Log.set(Log.LEVEL_DEBUG);
        new VoServer();
    }
}

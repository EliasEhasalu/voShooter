package ee.taltech.voshooter.networking.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.esotericsoftware.minlog.Log;

import ee.taltech.voshooter.networking.ClientInterface;
import ee.taltech.voshooter.networking.Network;
import ee.taltech.voshooter.networking.RemoteInterface;
import ee.taltech.voshooter.networking.messages.Lobby;
import ee.taltech.voshooter.networking.messages.User;

public class VoServer {

    Server server;

    private Random rand = new Random();

    private List<Remote> connectedUsers = new ArrayList<>();
    private List<Lobby> lobbies = new ArrayList<>();

    /**
     * Bootstrap the server upon instantiation.
     */
    public VoServer() throws IOException {
        server = new Server() {
            @Override
            protected Connection newConnection() {
                Remote remoteObject = new Remote();
                connectedUsers.add(remoteObject);
                return remoteObject;
            }
        };

        Network.register(server);

        server.addListener(new Listener() {
            @Override
            public void disconnected(Connection connection) {
                Remote remoteObject = (Remote) connection;
                remoteObject.purge();
            }
        });

        server.bind(Network.PORT);
        server.start();
    }

    /** Close the server upon request. */
    public void close() {
        try {
            server.dispose();
        } catch (IOException e) {
            //.
        }
    }

    public class Remote extends Connection implements RemoteInterface {

        private ClientInterface client;

        private User user = new User();
        private Lobby currentLobby;

        /**
         * Construct this user object.
         */
        public Remote() {
            new ObjectSpace(this).register(Network.REMOTE, this);
            client = ObjectSpace.getRemoteObject(this, Network.SERVER_ENTRY, ClientInterface.class);
        }

        /**
         * Set the User's name.
         * @param name The name to set the User's name to.
         */
        public void setUserName(String name) {
            user.setName(name);
        }

        /**
         * @return The created lobby.
         * @param numberOfPlayers The desired max amount of players in the lobby.
         * @param gameMode The desired gamemode for the lobby.
         */
        public Lobby createLobby(int numberOfPlayers, int gameMode) {
            Lobby newLobby = new Lobby(numberOfPlayers, gameMode, generateLobbyCode());
            lobbies.add(newLobby);
            newLobby.addUser(user);
            currentLobby = newLobby;
            return newLobby;
        }


        /** @return A unique lobby code for a newly created lobby. */
        private String generateLobbyCode() {
            String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String attempt = "";

            while (true) {
                for (int i = 0; i < 6; i++) {
                    attempt += abc.charAt(rand.ints(0, abc.length()).findFirst().getAsInt());
                }

                Set<String> codes = lobbies.stream()
                    .map(Lobby::getLobbyCode)
                    .collect(Collectors.toSet());

                if (codes.contains(attempt)) {
                    attempt = "";
                    continue;
                }
                break;
            }
            return attempt;
        }

        /** Have the user leave the lobby they are currently in. */
        public void leaveLobby() {
        }

        /**
         * Remove all state associated with this connection.
         */
        public void purge() {
            if (currentLobby != null) {
                currentLobby.removeUser(user);
            }
            connectedUsers.remove(this);
        }
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

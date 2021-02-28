package ee.taltech.voshooter.networking.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.esotericsoftware.minlog.Log;

import ee.taltech.voshooter.networking.Network;
import ee.taltech.voshooter.networking.UserComms;
import ee.taltech.voshooter.networking.messages.Lobby;

public class VoServer {

    Server server;

    private List<User> users = new ArrayList<>();
    private List<Lobby> lobbies = new ArrayList<>();

    /**
     * Bootstrap the server upon instantiation.
     */
    public VoServer() throws IOException {
        server = new Server() {
            @Override
            protected Connection newConnection() {
                User user = new User();
                users.add(user);
                return user;
            }
        };

        Network.register(server);

        server.addListener(new Listener() {
            @Override
            public void disconnected(Connection connection) {
                User user = (User) connection;
                users.remove(user);
            }
        });

        server.bind(Network.PORT);
        server.start();
    }

    public class User extends Connection implements UserComms {

        private int timesPinged = 0;

        /**
         * Construct this user object.
         */
        User() {
            new ObjectSpace(this).register(Network.USER, this);
        }

        /**
         * @return The amount of times this user has pinged the server.
         */
        public int ping() {
            return timesPinged++;
        }

        /**
         * @return A list of available lobbies.
         */
        public List<Lobby> getLobbies() {
            return lobbies;
        }

        /**
         * @return The created lobby.
         */
        public Lobby createLobby() {
            Lobby newLobby = new Lobby();
            newLobby.addUser(this);
            lobbies.add(newLobby);
            return newLobby;
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

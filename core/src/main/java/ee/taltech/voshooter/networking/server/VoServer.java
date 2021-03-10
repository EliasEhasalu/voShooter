package ee.taltech.voshooter.networking.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import ee.taltech.voshooter.networking.Network;
import ee.taltech.voshooter.networking.messages.CreateLobby;
import ee.taltech.voshooter.networking.messages.JoinLobby;
import ee.taltech.voshooter.networking.messages.LeaveLobby;
import ee.taltech.voshooter.networking.messages.LobbyFull;
import ee.taltech.voshooter.networking.messages.LobbyJoined;
import ee.taltech.voshooter.networking.messages.NoSuchLobby;
import ee.taltech.voshooter.networking.messages.SetUsername;
import ee.taltech.voshooter.networking.messages.User;

public class VoServer {

    Server server;

    private int playerId = 0;
    private Random rand = new Random();

    private Set<VoConnection> connections = new HashSet<>();
    private Map<String, Lobby> lobbies = new HashMap<>();

    /**
     * Construct the server.
     */
    public VoServer() throws IOException {
        server = new Server() {
            @Override
            protected VoConnection newConnection() {
                return new VoConnection();
            }
        };

        Network.register(server);

        server.addListener(new Listener() {

            @Override
            public void received(Connection c, Object message) {
                VoConnection connection = (VoConnection) c;
                if (connection.user == null) {
                    connection.user = new User();
                    connection.user.id = playerId++;
                }
                connections.add(connection);
                User user = connection.user;


                if (message instanceof SetUsername) {
                    user.name = ((SetUsername) message).desiredName;
                    return;
                }

                if (message instanceof CreateLobby) {
                    CreateLobby req = (CreateLobby) message;
                    String code = generateLobbyCode();
                    Lobby newLobby = new Lobby(req.gameMode, req.maxPlayers, code);
                    newLobby.setHost(connection);
                    lobbies.put(code, newLobby);

                    if (newLobby.addConnection(connection)) {
                       user.currentLobby = code;
                    }

                    LobbyJoined res = new LobbyJoined(req.gameMode, req.maxPlayers, code, newLobby.getUsers(), user);
                    c.sendTCP(res);
                    return;
                }

                if (message instanceof JoinLobby) {
                    String code = ((JoinLobby) message).lobbyCode.trim().toUpperCase();
                    if (code != null && code != "" && lobbies.containsKey(code)) {
                        Lobby lobby = lobbies.get(code);
                        if (!lobby.addConnection(connection)) {
                            connection.sendTCP(new LobbyFull());
                        } else {
                            int gameMode = lobby.getGameMode();
                            int maxPlayers = lobby.getMaxPlayers();
                            List<User> users = lobby.getUsers();
                            User host = lobby.getHost().user;
                            user.currentLobby = code;

                            connection.sendTCP(new LobbyJoined(gameMode, maxPlayers, code, users, host));
                            lobby.addConnection(connection);
                        }
                    } else {
                        connection.sendTCP(new NoSuchLobby());
                    }
                    return;
                }

                if (message instanceof LeaveLobby) {
                    Lobby lobby = lobbies.get(user.currentLobby);
                    lobby.removeConnection(connection);

                    if (lobby.getPlayerCount() == 0) {
                        lobbies.remove(lobby.getLobbyCode());
                    }
                }
            }

            @Override
            public void disconnected(Connection c) {
               VoConnection connection = (VoConnection) c;
               if (connection != null) {
                   if (connections.contains(connection)) connections.remove(connection);
                   if (connection.user != null && connection.user.currentLobby != null) {
                       Lobby lobby = lobbies.get(connection.user.currentLobby);
                       lobby.removeConnection(connection);
                   }
               }
            }
        });

        // server.addListener(
        // new RunMethodListener<LeaveLobby>(LeaveLobby.class) {
        //     @Override
        //     public void run(Connection c, LeaveLobby msg) {
        //     }
        // });

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

    /** @return A unique lobby code for a newly created lobby. */
    private String generateLobbyCode() {
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String attempt = "";

        while (true) {
            for (int i = 0; i < 6; i++) {
                attempt += abc.charAt(rand.ints(0, abc.length()).findFirst().getAsInt());
            }

            Set<String> codes = lobbies.values().stream()
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

    static class VoConnection extends Connection {
        public User user;
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

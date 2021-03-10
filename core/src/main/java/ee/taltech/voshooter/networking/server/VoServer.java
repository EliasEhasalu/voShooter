package ee.taltech.voshooter.networking.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import ee.taltech.voshooter.networking.Network;
import ee.taltech.voshooter.networking.messages.User;
import ee.taltech.voshooter.networking.messages.clientreceived.LobbyFull;
import ee.taltech.voshooter.networking.messages.clientreceived.LobbyJoined;
import ee.taltech.voshooter.networking.messages.clientreceived.NoSuchLobby;
import ee.taltech.voshooter.networking.messages.serverreceived.CreateLobby;
import ee.taltech.voshooter.networking.messages.serverreceived.JoinLobby;
import ee.taltech.voshooter.networking.messages.serverreceived.LeaveLobby;
import ee.taltech.voshooter.networking.messages.serverreceived.SetUsername;
import ee.taltech.voshooter.networking.messages.serverreceived.StartGame;

public class VoServer {

    Server server;

    private long playerId = 0;
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

        server.addListener(
        new RunMethodListener<SetUsername>(SetUsername.class) {
            @Override
            public void run(VoConnection c, SetUsername msg) {
                handleSetUsername(c, msg);
            }
        });

        server.addListener(new Listener() {
            @Override
            public void disconnected(Connection c) {
                handleDisconnects((VoConnection) c);
            }
        });

        server.addListener(
        new RunMethodListener<CreateLobby>(CreateLobby.class) {
            @Override
            public void run(VoConnection c, CreateLobby msg) {
                handleCreateLobby(c, msg);
            }
        });

        server.addListener(
        new RunMethodListener<JoinLobby>(JoinLobby.class) {
            @Override
            public void run(VoConnection c, JoinLobby msg) {
                handleJoinLobby(c, msg);
            }
        });

        server.addListener(
        new RunMethodListener<LeaveLobby>(LeaveLobby.class) {
            @Override
            public void run(VoConnection c, LeaveLobby msg) {
                handleLeaveLobby(c);
            }
        });

        server.addListener(
        new RunMethodListener<StartGame>(StartGame.class) {
            @Override
            public void run(VoConnection c, StartGame msg) {
                handleStartGame(c);
            }
        });

        server.bind(Network.PORT);
        server.start();
    }

    /**
     * Handle creating lobbies.
     * @param connection The connection.
     * @param msg The message.
     */
    private void handleCreateLobby(VoConnection connection, CreateLobby msg) {
        String code = generateLobbyCode();
        User user = connection.user;
        Lobby newLobby = new Lobby(msg.gameMode, msg.maxPlayers, code);
        newLobby.setHost(connection);
        lobbies.put(code, newLobby);

        if (newLobby.addConnection(connection)) {
           user.currentLobby = code;
        }

        LobbyJoined res = new LobbyJoined(msg.gameMode, msg.maxPlayers, code, newLobby.getUsers(), user);
        connection.sendTCP(res);
    }

    /**
     * @param connection The connection.
     * @param msg The JoinLobby message {@link JoinLobby}
     */
    private void handleJoinLobby(VoConnection connection, JoinLobby msg) {
        String code = msg.lobbyCode.trim().toUpperCase();
        User user = connection.user;

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
    }

    /**
     * @param connection The connection.
     */
    private void handleLeaveLobby(VoConnection connection) {
        Lobby lobby = lobbies.get(connection.user.currentLobby);
        lobby.removeConnection(connection);

        if (lobby.getPlayerCount() == 0) {
            lobbies.remove(lobby.getLobbyCode());
        }
    }

    /**
     * Start a game upon valid request.
     * @param connection The connection that sent the start game request.
     */
    private void handleStartGame(VoConnection connection) {
        User user = connection.user;
        Optional<Lobby> optLobby = getUserLobby(user);

        if (optLobby.isPresent()) {
            Lobby lobby = optLobby.get();

            if (lobby.getHost() == connection && lobby.getPlayerCount() >= Lobby.MINIMUM_PLAYERS) {
                lobby.sendGameStart();
            }
        }
    }

    /**
     * @param connection The connection.
     * @param msg The message with the desired username.
     */
    private void handleSetUsername(VoConnection connection, SetUsername msg) {
        handleNewConnections(connection);
        if (verifyUsername(msg.desiredName)) connection.user.name = msg.desiredName;
    }

    /**
     * Create and save a User object if one doesn't already exist for this connection.
     * @param connection The connection being examined.
     */
    private void handleNewConnections(VoConnection connection) {
        if (connection.user == null) {
            connection.user = new User();
            connection.user.id = playerId++;
        }
        if (!connections.contains(connection)) connections.add(connection);
    }

    /**
     * Handle severed connections.
     * @param connection The connection that disconnected.
     */
    private void handleDisconnects(VoConnection connection) {
       if (connection != null) {
           if (connections.contains(connection)) connections.remove(connection);
           if (connection.user != null && connection.user.currentLobby != null) {
               Lobby lobby = lobbies.get(connection.user.currentLobby);
               lobby.removeConnection(connection);
           }
       }
    }

    /**
     * @param username The username to check.
     * @return Whether the username is acceptable.
     */
    private boolean verifyUsername(String username) {
        return (
            !username.trim().equals("")
            && username != null
            && username.length() >= 4
            && username.length() <= 12
        );
    }

    /**
     * Return the lobby object the specified user is in.
     * @return The lobby object the user is in.
     * @param user The examined user.
     */
    private Optional<Lobby> getUserLobby(User user) {
        String lobbyCode = user.currentLobby;

        if (!lobbyExists(lobbyCode)) return Optional.empty();
        return Optional.of(lobbies.get(lobbyCode));
    }

    /**
     * @param lobbyCode The lobby code queried.
     * @return Whether a specified lobby exists.
     */
    private boolean lobbyExists(String lobbyCode) {
        return (lobbyCode != null && lobbies.containsKey(lobbyCode));
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

    /** Close the server upon request. */
    public void close() {
        try {
            server.dispose();
        } catch (IOException e) {
            //.
        }
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

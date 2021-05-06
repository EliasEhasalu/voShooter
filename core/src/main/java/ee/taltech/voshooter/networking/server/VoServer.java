package ee.taltech.voshooter.networking.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import ee.taltech.voshooter.map.GameMap;
import ee.taltech.voshooter.networking.Network;
import ee.taltech.voshooter.networking.messages.User;
import ee.taltech.voshooter.networking.messages.clientreceived.ChatReceiveMessage;
import ee.taltech.voshooter.networking.messages.clientreceived.LobbyFull;
import ee.taltech.voshooter.networking.messages.clientreceived.LobbyJoined;
import ee.taltech.voshooter.networking.messages.clientreceived.NoSuchLobby;
import ee.taltech.voshooter.networking.messages.serverreceived.ChatSendMessage;
import ee.taltech.voshooter.networking.messages.serverreceived.CreateLobby;
import ee.taltech.voshooter.networking.messages.serverreceived.JoinLobby;
import ee.taltech.voshooter.networking.messages.serverreceived.LeaveLobby;
import ee.taltech.voshooter.networking.messages.serverreceived.LobbySettingsChanged;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerInput;
import ee.taltech.voshooter.networking.messages.serverreceived.SetUsername;
import ee.taltech.voshooter.networking.messages.serverreceived.StartGame;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class VoServer {

    Server server;

    private long playerId = 0;
    private final Random rand = new Random();

    private final Set<VoConnection> connections = new HashSet<>();
    private final Map<String, Lobby> lobbies = new ConcurrentHashMap<>();

    /**
     * Construct the server.
     */
    public VoServer(int port) throws IOException {
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

        server.addListener(
        new RunMethodListener<PlayerInput>(PlayerInput.class) {
            @Override
            public void run(VoConnection c, PlayerInput msg) {
                handlePlayerInput(c, msg);
            }
        });

        server.addListener(
        new RunMethodListener<ChatSendMessage>(ChatSendMessage.class) {
            @Override
            public void run(VoConnection c, ChatSendMessage msg) {
                    handleSendMessage(c, msg);
                }
        });

        server.addListener(
        new RunMethodListener<LobbySettingsChanged>(LobbySettingsChanged.class) {
            @Override
            public void run(VoConnection c, LobbySettingsChanged msg) {
                handleLobbyChanges(msg);
            }
        });

        server.bind(port);
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
        Lobby newLobby = new Lobby(code);
        lobbies.put(code, newLobby);

        newLobby.addConnection(connection);
        newLobby.setHost(connection);

        connection.sendTCP(new LobbyJoined(newLobby.getGameMode(), newLobby.getMaxPlayers(), code, newLobby.getUsers(), user,
                        user.id, newLobby.getMapType()));
    }

    private void handleLobbyChanges(LobbySettingsChanged msg) {
        String code = sanitizeLobbyCode(msg.lobbyCode);
        if (lobbyExists(code)) {
            Lobby lobby = lobbies.get(code);
            lobby.handleChanges(msg);
        }
    }

    /**
     * @param connection The connection.
     * @param msg The JoinLobby message {@link JoinLobby}
     */
    private void handleJoinLobby(VoConnection connection, JoinLobby msg) {
        String code = sanitizeLobbyCode(msg.lobbyCode);

        if (lobbyExists(code)) {
            Lobby lobby = lobbies.get(code);

            if (lobby.isFull()) {
                connection.sendTCP(new LobbyFull());
            } else {
                int gameMode = lobby.getGameMode();
                int maxPlayers = lobby.getMaxPlayers();
                List<User> users = lobby.getUsers();
                User host = lobby.getHost().user;
                GameMap.MapType mapType = lobby.getMapType();

                connection.sendTCP(
                        new LobbyJoined(gameMode, maxPlayers, code, users, host, connection.user.id, mapType));
                lobby.addConnection(connection);
            }
        } else {
            connection.sendTCP(new NoSuchLobby(code));
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
     * Handle PlayerInput messages.
     * @param c The connection.
     * @param msg The PlayerInput message.
     */
    private void handlePlayerInput(VoConnection c, PlayerInput msg) {
        // If the a lobby exists, pass the PlayerInput message to
        // that lobby's Game object. Physics / game logic will be handled there.
        Optional<Lobby> optLobby = getUserLobby(c.user);

        if (optLobby.isPresent()) {
            Lobby lobby = optLobby.get();

            if (lobby.getGame() != null) {
                lobby.getGame().addPlayerInput(c, msg);
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
        connections.add(connection);
    }

    /**
     * @param connection The connection.
     */
    private void handleLeaveLobby(VoConnection connection) {
        handleDisconnects(connection);
    }

    /**
     * Handle severed connections.
     * @param connection The connection that disconnected.
     */
    private void handleDisconnects(VoConnection connection) {
       if (connection != null) {
           connections.remove(connection);
           if (connection.user != null && connection.user.currentLobby != null) {
               Lobby lobby = lobbies.get(connection.user.currentLobby);
               lobby.removeConnection(connection);
               if (lobby.getPlayerCount() == 0) {
                   if (lobby.getGame() != null) {
                       lobby.getGame().shutDown();
                   }
                   if (lobbyExists(lobby.getLobbyCode())) {
                       lobbies.remove(lobby.getLobbyCode());
                   }
               }
           }
       }
    }

    /**
     * Handle sending chat messages.
     * @param connection The connection that sent the chat message.
     * @param msg The message object.
     */
    private void handleSendMessage(VoConnection connection, ChatSendMessage msg) {
        Optional<Lobby> optLobby = getUserLobby(connection.user);

        if (optLobby.isPresent()) {
            Lobby lobby = optLobby.get();

            if (lobby.getGame() != null) {
                ChatReceiveMessage chatMsg = new ChatReceiveMessage(connection.user.id, msg.message);
                for (Connection c : lobby.getConnections()) {
                    c.sendTCP(chatMsg);
                }
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
            && username.length() >= 4
            && username.length() <= 12
        );
    }

    /**
     * @param code The code to check.
     * @return Whether the given code points to a lobby or not.
     */
    private String sanitizeLobbyCode(String code) {
        if (code == null) return "";
        return code.trim().toUpperCase();
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

    /**
     * Main entry point for server.
     * @param args CLI args.
     */
    public static void main(String[] args) throws IOException {
        int port = 5001;
        if (args.length > 0) port = Integer.parseInt(args[0]);

        Log.set(Log.LEVEL_INFO);
        new VoServer(port);
    }
}

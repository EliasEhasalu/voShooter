package ee.taltech.voshooter.networking.server;

import ee.taltech.voshooter.map.GameMap;
import ee.taltech.voshooter.networking.messages.User;
import ee.taltech.voshooter.networking.messages.clientreceived.GameStarted;
import ee.taltech.voshooter.networking.messages.clientreceived.LobbyUserUpdate;
import ee.taltech.voshooter.networking.messages.serverreceived.LobbySettingsChanged;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.lang.Math.min;

public class Lobby {

    public static final int MINIMUM_PLAYERS = 1;
    public static final int MAX_BOTS = 8;
    public static final int MAX_USERS = 8;

    private int maxUsers = 4;
    private int botAmount = 0;
    private int gameMode = 1;
    private final String lobbyCode;
    private int gameLength;
    private VoConnection host;
    private GameMap.MapType mapType = GameMap.MapType.DEFAULT;

    private Game game;
    private VoServer parent;
    private final Set<VoConnection> connections = ConcurrentHashMap.newKeySet();

    /**
     * @param parent VoServer that hosts the lobby.
     * @param lobbyCode The lobby code assigned to this lobby.
     */
    protected Lobby(VoServer parent, String lobbyCode) {
        this.parent = parent;
        this.lobbyCode = lobbyCode;
    }

    /** Send updates of people joining / leaving to this lobby's members. */
    private void sendLobbyUpdates() {
        List<User> users = getUsers();
        List<Player> players;
        if (game != null && game.isAlive()) players = game.getPlayers();
        else players = getPlayers();

        for (VoConnection con : connections) {
            con.sendTCP(new LobbyUserUpdate(users, players));
        }
    }

    /** Send all users in this lobby a message that the game has started. */
    protected void sendGameStart() {
        game = new Game(parent, gameMode, mapType, gameLength);
        for (VoConnection con : connections) game.addConnection(con);
        for (int i = 0; i < botAmount; i++) game.addBot();

        game.start();

        for (VoConnection con : connections) {
            con.sendTCP(new GameStarted(game.getPlayers()));
        }
    }

    /**
     * Add a user to this lobby.
     * @return Whether adding the user was successful.
     * @param connection The user to add to this lobby.
     */
    protected boolean addConnection(VoConnection connection) {
        if (connections.contains(connection) || connections.size() == maxUsers) {
            return false;
        }

        connections.add(connection);
        connection.user.currentLobby = getLobbyCode();

        System.out.printf("Added ID %d: %s to lobby %s.%n", connection.user.id, connection.user.name, lobbyCode);
        sendLobbyUpdates();
        if (game != null) {
            game.addConnection(connection);
            for (VoConnection con : connections) {
                con.sendTCP(new GameStarted(game.getPlayers()));
            }
        }
        return true;
    }

    /**
     * Remove the given user from this lobby.
     * @param connection The user to remove.
     * @return If the user was removed.
     */
    protected boolean removeConnection(VoConnection connection) {
        if (connections.contains(connection)) {
            connections.remove(connection);
            if (game != null) game.removeConnection(connection);
            connection.user.currentLobby = null;
            connection.user.host = false;

            // If host left, assign someone else as host.
            if (getHost().user == connection.user && !connections.isEmpty()) {
                setHost(connections.iterator().next());
            }

            System.out.printf("Removed ID %d: %s from lobby %s.%n",
                    connection.user.id, connection.user.name, lobbyCode);
            sendLobbyUpdates();
            return true;
        }
        return false;
    }

    /** @return Whether the lobby is full or not. */
    protected boolean isFull() {
        return (connections.size() == maxUsers);
    }

    /**
     * @param name of the joining player.
     * @return whether the name already exists.
     */
    protected boolean isRepeatingName(String name) {
        for (VoConnection connection : connections) {
            if (connection.user.name.equals(name)) return true;
        }
        return false;
    }

    /** @return Amount of players in this lobby. */
    protected int getPlayerCount() {
        return connections.size();
    }

    /**
     * @return A list of user objects in this lobby.
     */
    protected List<User> getUsers() {
        return connections.stream()
            .map(con -> con.user)
            .collect(Collectors.toList());
    }

    public void handleChanges(LobbySettingsChanged msg) {
        this.gameMode = msg.gameMode;
        this.mapType = msg.mapType;
        this.gameLength = msg.gameLength;
        this.maxUsers = min(msg.maxUsers, MAX_USERS);
        this.botAmount = min(msg.botAmount, MAX_BOTS);
        for (VoConnection connection : connections) {
            connection.sendTCP(msg);
        }
    }

    /**
     * @return A list of players in this lobby.
     */
    protected List<Player> getPlayers() {
        return connections.stream().map(con -> con.player).collect(Collectors.toList());
    }

    /** @return The game mode. */
    protected int getGameMode() {
        return gameMode;
    }

    /** @return The game map used in this lobby. */
    protected GameMap.MapType getMapType() {
        return mapType;
    }

    /**
     * Set the host for this lobby.
     * @param connection The connection to set the host as.
     */
    protected void setHost(VoConnection connection) {
        connection.user.setHost(true);
        this.host = connection;
    }

    /** @return The host. */
    protected VoConnection getHost() {
        return host;
    }

    /** @return Max amount of players in this lobby. */
    protected int getMaxPlayers() {
        return maxUsers;
    }

    /** @return This lobby's code. */
    protected String getLobbyCode() {
        return lobbyCode;
    }

    /** @return A list of this lobby's users. */
    protected List<VoConnection> getConnections() {
        return new ArrayList<>(connections);
    }

    /** @return The game object in this lobby. */
    protected Game getGame() {
        return game;
    }

    public int getGameLength() {
        return gameLength;
    }

    public int getBotAmount() {
        return botAmount;
    }
}

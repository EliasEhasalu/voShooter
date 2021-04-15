package ee.taltech.voshooter.networking.server;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.messages.User;
import ee.taltech.voshooter.networking.messages.clientreceived.GameStarted;
import ee.taltech.voshooter.networking.messages.clientreceived.LobbyUserUpdate;
import ee.taltech.voshooter.networking.server.gamestate.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Lobby {

    public static final int MINIMUM_PLAYERS = 2;

    private final int maxUsers;
    private final int gameMode;
    private final String lobbyCode;
    private VoConnection host;

    private Game game;
    private final Set<VoConnection> connections = ConcurrentHashMap.newKeySet();

    /**
     * @param maxUsers The maximum amount of users that can be in this lobby.
     * @param gameMode An integer representing the game mode of this lobby.
     * @param lobbyCode The lobby code assigned to this lobby.
     */
    protected Lobby(int gameMode, int maxUsers, String lobbyCode) {
        this.maxUsers = maxUsers;
        this.gameMode = gameMode;
        this.lobbyCode = lobbyCode;
    }

    /** Send updates of people joining / leaving to this lobby's members. */
    private void sendLobbyUpdates() {
        List<User> users = getUsers();
        List<Player> players = getPlayers();
        for (VoConnection con : connections) {
            con.sendTCP(new LobbyUserUpdate(users, players));
        }
    }

    /** Send all users in this lobby a message that the game has started. */
    protected void sendGameStart() {
        game = new Game(gameMode);

        for (VoConnection con : connections) {
            game.addConnection(con);
        }

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

        System.out.printf("added ID %d: %s to lobby %s%n", connection.user.id, connection.user.name, lobbyCode);
        sendLobbyUpdates();
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
            game.removeConnection(connection);
            connection.user.currentLobby = null;
            connection.user.host = false;

            // If host left, assign someone else as host.
            if (getHost().user == connection.user && !connections.isEmpty()) {
                setHost(connections.iterator().next());
            }

            System.out.printf("removed ID %d: %s from lobby %s%n", connection.user.id, connection.user.name, lobbyCode);
            sendLobbyUpdates();
            return true;
        }
        return false;
    }

    /** @return Whether the lobby is full or not. */
    protected boolean isFull() {
        return (connections.size() == maxUsers);
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
}

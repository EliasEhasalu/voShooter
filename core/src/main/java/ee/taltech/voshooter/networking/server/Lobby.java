package ee.taltech.voshooter.networking.server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.networking.messages.User;
import ee.taltech.voshooter.networking.messages.clientreceived.GameStarted;
import ee.taltech.voshooter.networking.messages.clientreceived.LobbyUserUpdate;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.geometry.Pos;

public class Lobby {

    public static final int MINIMUM_PLAYERS = 2;

    private int maxUsers;
    private int gameMode;
    private String lobbyCode;
    private VoConnection host;

    private Game game;
    private List<VoConnection> connections = new ArrayList<>();

    /**
     * @param maxUsers The maximum amount of users that can be in this lobby.
     * @param gameMode An integer representing the gamemode of this lobby.
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
        for (VoConnection con : connections) {
            con.sendTCP(new LobbyUserUpdate(users));
        }
    }

    /** Send all users in this lobby a message that the game has started. */
    protected void sendGameStart() {
        game = new Game();

        for (VoConnection con : connections) {
            con.player = new Player(new Pos(300, 300));
            game.addConnection(con);
        }

        game.start();

        for (VoConnection con : connections) {
            con.sendTCP(new GameStarted());
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

        System.out.println(String.format("added ID %d to lobby %s", connection.user.id, lobbyCode));
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
            connection.user.currentLobby = null;
            connection.user.host = false;

            // If host left, assign someone else as host.
            if (getHost().user == connection.user && !connections.isEmpty()) {
                setHost(connections.get(0));
            }

            System.out.println(String.format("removed %s from lobby %s", connection.user.name, lobbyCode));
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
        return connections;
    }

    /** @return The game object in this lobby. */
    protected Game getGame() {
        return game;
    }
}

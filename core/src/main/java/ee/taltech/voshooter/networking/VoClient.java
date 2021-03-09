package ee.taltech.voshooter.networking;


import java.io.IOException;
import java.util.List;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;

import ee.taltech.voshooter.VoShooter;
import ee.taltech.voshooter.networking.messages.LobbyCreated;
import ee.taltech.voshooter.networking.messages.LobbyEntry;
import ee.taltech.voshooter.networking.messages.LobbyJoined;
import ee.taltech.voshooter.networking.messages.LobbyUserUpdate;
import ee.taltech.voshooter.networking.messages.User;


public class VoClient {

    public User clientUser = new User();
    public VoShooter parent;
    public Client client;

    private static final String HOST_ADDRESS = "localhost";
    private static final int MILLISECONDS_BEFORE_TIMEOUT = 5000;

    /**
     * Construct the client.
     * @param parent A reference to the orchestrator object.
     */
    public VoClient(VoShooter parent) throws IOException {
        this.parent = parent;
        client = new Client();
        client.start();

        // Agree on what messages should be passed.
        Network.register(client);

        client.addListener(new ThreadedListener(new Listener() {
            @Override
            public void connected(Connection connection) {
            }

            @Override
            public void received(Connection connection, Object message) {

                if (message instanceof LobbyCreated) {
                    LobbyCreated mes = (LobbyCreated) message;
                    joinCreatedLobby(mes.entry);
                    return;
                }

                if (message instanceof LobbyJoined) {
                    LobbyJoined mes = (LobbyJoined) message;
                    joinLobby(mes.entry);
                    return;
                }

                if (message instanceof LobbyUserUpdate) {
                    LobbyUserUpdate update = (LobbyUserUpdate) message;
                    updateLobby(update);
                    return;
                }
            }
        }));

        client.connect(MILLISECONDS_BEFORE_TIMEOUT, HOST_ADDRESS, Network.PORT);
    }

    /**
     * Handle joining a created lobby after receiving a response from server.
     * @param mes The message from the server.
     */
    private void joinCreatedLobby(LobbyEntry mes) {
            parent.gameState.clientUser.setHost(true);
            parent.gameState.currentLobby.handleJoining(mes);
            parent.createGameScreen.shouldChangeScreen = VoShooter.Screen.LOBBY;
    }

    /**
     * Join a lobby.
     * @param mes The message describing the information about the lobby.
     */
    private void joinLobby(LobbyEntry mes) {
            parent.gameState.currentLobby.handleJoining(mes);
            parent.joinGameScreen.shouldChangeScreen = VoShooter.Screen.LOBBY;
    }

    /**
     * Update users currently in lobby.
     * @param update Update containing users currently in the lobby.
     */
    private void updateLobby(LobbyUserUpdate update) {
        List<User> users = update.users;
        parent.gameState.currentLobby.setUsers(users);
        System.out.println(users);
    }
}

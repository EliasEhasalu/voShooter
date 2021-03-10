package ee.taltech.voshooter.networking;


import java.io.IOException;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;

import ee.taltech.voshooter.VoShooter;
import ee.taltech.voshooter.networking.messages.LobbyJoined;
import ee.taltech.voshooter.networking.messages.LobbyUserUpdate;
import ee.taltech.voshooter.networking.messages.User;


public class VoClient {

    public User clientUser = new User();
    public VoShooter parent;
    private Client client;
    ServerEntry serverEntry;

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
            private VoShooter.Screen screenToChangeTo;

            @Override
            public void connected(Connection connection) {
            }

            @Override
            public void run() {
                try {
                    client.connect(MILLISECONDS_BEFORE_TIMEOUT, HOST_ADDRESS, Network.PORT);
                } catch (IOException ex) {
                    client.close();
                }

                // Define actions to be taken on the next cycle
                // of the OpenGL rendering thread.
                Gdx.app.postRunnable(new Runnable() {
                    public void run() {
                        if (screenToChangeTo != null) {
                            parent.changeScreen(screenToChangeTo);
                            screenToChangeTo = null;
                        }
                    }
                });
            }
        }));

        client.connect(MILLISECONDS_BEFORE_TIMEOUT, HOST_ADDRESS, Network.PORT);
    }

    /** @return Whether or not client is connected. */
    public boolean isConnected() {
        return client.isConnected();
    }

    /**
     * Update users currently in lobby.
     * @param update Update containing users currently in the lobby.
     */
    private void updateLobby(LobbyUserUpdate update) {
        List<User> users = update.users;
        parent.gameState.currentLobby.setUsers(users);
    }
}

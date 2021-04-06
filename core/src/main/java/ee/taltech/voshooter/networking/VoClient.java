package ee.taltech.voshooter.networking;


import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import ee.taltech.voshooter.VoShooter;
import ee.taltech.voshooter.entity.player.ClientPlayer;
import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.networking.messages.User;
import ee.taltech.voshooter.networking.messages.clientreceived.GameStarted;
import ee.taltech.voshooter.networking.messages.clientreceived.LobbyJoined;
import ee.taltech.voshooter.networking.messages.clientreceived.LobbyUserUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.NoSuchLobby;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerDead;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerDeath;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerHealthUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerPositionUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerViewUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectileCreated;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectileDestroyed;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectilePositions;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


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
            private VoShooter.Screen screenToChangeTo;
            private GameStarted gameStart;
            private Set<ProjectileCreated> projectilesCreatedSet = ConcurrentHashMap.newKeySet();
            private Set<ProjectileDestroyed> projectileDestroyedSet = ConcurrentHashMap.newKeySet();
            private ProjectilePositions projectileUpdate;

            @Override
            public void connected(Connection connection) {
            }

            @Override
            public void received(Connection connection, Object message) {

                if (message instanceof LobbyJoined) {
                    LobbyJoined mes = (LobbyJoined) message;
                    screenToChangeTo = VoShooter.Screen.LOBBY;
                    joinLobby(mes);
                } else if (message instanceof LobbyUserUpdate) {
                    LobbyUserUpdate update = (LobbyUserUpdate) message;
                    updateLobby(update);
                } else if (message instanceof GameStarted) {
                    gameStart = (GameStarted) message;
                    screenToChangeTo = VoShooter.Screen.MAIN;
                } else if (message instanceof PlayerPositionUpdate) {
                    updatePlayerPositions((PlayerPositionUpdate) message);
                } else if (message instanceof PlayerViewUpdate) {
                    updatePlayerViewDirections((PlayerViewUpdate) message);
                } else if (message instanceof ProjectileCreated) {
                    projectilesCreatedSet.add((ProjectileCreated) message);
                } else if (message instanceof ProjectileDestroyed) {
                    projectileDestroyedSet.add((ProjectileDestroyed) message);
                } else if (message instanceof ProjectilePositions) {
                   projectileUpdate = (ProjectilePositions) message;
                } else if (message instanceof NoSuchLobby) {
                    parent.setCodeCorrect(false);
                    parent.setLobbyCode(((NoSuchLobby) message).lobbyCode);
                } else if (message instanceof PlayerHealthUpdate) {
                    updatePlayerHealth((PlayerHealthUpdate) message);
                } else if (message instanceof PlayerDeath) {
                    // TODO method.
                } else if (message instanceof PlayerDead) {
                    // TODO method.
                }

                // Define actions to be taken on the next cycle
                // of the OpenGL rendering thread.
                Gdx.app.postRunnable(new Runnable() {
                    public void run() {
                        if (screenToChangeTo != null) {
                            parent.changeScreen(screenToChangeTo);
                            screenToChangeTo = null;
                        }
                        if (gameStart != null) {
                            createPlayerObjects(gameStart);
                            gameStart = null;
                        }
                        if (projectileUpdate != null) {
                            updateProjectilePositions(projectileUpdate);
                            projectileUpdate = null;
                        }
                        if (!projectilesCreatedSet.isEmpty()) {
                            for (ProjectileCreated msg : projectilesCreatedSet) {
                                createProjectile(msg);
                                projectilesCreatedSet.remove(msg);
                            }
                        }
                        if (!projectileDestroyedSet.isEmpty()) {
                            for (ProjectileDestroyed msg : projectileDestroyedSet) {
                                destroyProjectile(msg);
                                projectileDestroyedSet.remove(msg);
                            }
                        }
                    }
                });
            }
        }));

        client.connect(MILLISECONDS_BEFORE_TIMEOUT, HOST_ADDRESS, Network.PORT);
    }

    /**
     * Join a lobby.
     * @param msg The message describing the information about the lobby.
     */
    private void joinLobby(LobbyJoined msg) {
            parent.gameState.currentLobby.handleJoining(msg);
    }

    /**
     * Update users currently in lobby.
     * @param update Update containing users currently in the lobby.
     */
    private void updateLobby(LobbyUserUpdate update) {
        List<User> users = update.users;
        List<Player> players = update.players;
        parent.gameState.currentLobby.setUsers(users);
        if (players != null) {
            parent.gameState.currentLobby.setPlayers(players);
            parent.gameState.updatePlayers(players);
        }
    }

    /**
     * Create the player objects in lobby.
     * @param msg Message containing Player objects.
     */
    private void createPlayerObjects(GameStarted msg) {
        parent.gameState.createPlayerObjects(msg.players);
    }

    /**
     * Update players' positions.
     * @param msg The message containing information about player positions.
     */
    private void updatePlayerPositions(PlayerPositionUpdate msg) {
        for (ClientPlayer p : parent.gameState.players) {
            if (p.getId() == msg.id) {
                p.setPos(msg.pos);
            }
        }
    }

    /**
     * Update players' health.
     * @param msg The message containing info about player health.
     */
    private void updatePlayerHealth(PlayerHealthUpdate msg) {
        for (ClientPlayer p : parent.gameState.players) {
            if (p.getId() == msg.id) {
                p.setHealth(msg.health);
            }
        }
    }

    /**
     * Update the players' view directions.
     * @param msg The message describing the poses of the players.
     */
    private void updatePlayerViewDirections(PlayerViewUpdate msg) {
        for (ClientPlayer p : parent.gameState.players) {
            if (p.getId() == msg.id) {
                p.getSprite().setRotation(msg.viewDirection.angleDeg());
            }
        }
    }

    /**
     * Create a new projectile.
     * @param msg Projectile created message.
     */
    private void createProjectile(ProjectileCreated msg) {
        parent.gameState.createProjectile(msg);
    }

    /**
     * Destroy a projectile.
     * @param msg Projectile destroyed message.
     */
    private void destroyProjectile(ProjectileDestroyed msg) {
        parent.gameState.destroyProjectile(msg);
    }

    /**
     * Update the positions of the projectiles.
     * @param msg Update message.
     */
    private void updateProjectilePositions(ProjectilePositions msg) {
        parent.gameState.updateProjectiles(msg);
    }
}

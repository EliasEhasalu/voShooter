package ee.taltech.voshooter.networking;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import ee.taltech.voshooter.VoShooter;
import ee.taltech.voshooter.entity.clientprojectile.ClientProjectile;
import ee.taltech.voshooter.entity.player.ClientPlayer;
import ee.taltech.voshooter.gamestate.ChatEntry;
import ee.taltech.voshooter.gamestate.gamemode.ClientKingOfTheHillManager;
import ee.taltech.voshooter.networking.messages.User;
import ee.taltech.voshooter.networking.messages.clientreceived.ChatGamePlayerChange;
import ee.taltech.voshooter.networking.messages.clientreceived.ChatReceiveMessage;
import ee.taltech.voshooter.networking.messages.clientreceived.GameEnd;
import ee.taltech.voshooter.networking.messages.clientreceived.GameStarted;
import ee.taltech.voshooter.networking.messages.clientreceived.LobbyJoined;
import ee.taltech.voshooter.networking.messages.clientreceived.LobbyUserUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.NoSuchLobby;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerAmmoUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerDashed;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerDead;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerDeath;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerHealthUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerKothChange;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerKothScores;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerPositionUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerStatistics;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerSwappedWeapon;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerViewUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectileCreated;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectileDestroyed;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectilePositions;
import ee.taltech.voshooter.networking.messages.clientreceived.RailgunFired;
import ee.taltech.voshooter.networking.messages.serverreceived.LobbySettingsChanged;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.screens.MainScreen;
import ee.taltech.voshooter.soundeffects.SoundPlayer;
import ee.taltech.voshooter.weapon.projectile.Projectile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class VoClient {

    public User clientUser = new User();
    public VoShooter parent;
    public Client client;

    private static final int MILLISECONDS_BEFORE_TIMEOUT = 5000;

    /**
     * Construct the client.
     * @param parent A reference to the orchestrator object.
     */
    public VoClient(VoShooter parent, String address, int port) throws IOException {
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
            private Set<RailgunFired> railgunFiredSet = ConcurrentHashMap.newKeySet();
            private Set<PlayerDeath> playerDeathSet = ConcurrentHashMap.newKeySet();
            private ProjectilePositions projectileUpdate;
            private Set<ChatReceiveMessage> receivedMessages = ConcurrentHashMap.newKeySet();
            private Set<ChatGamePlayerChange> receivedPlayerChanges = ConcurrentHashMap.newKeySet();
            private Set<PlayerDashed> playerDashedMessages = ConcurrentHashMap.newKeySet();
            private GameEnd gameEnd;

            @Override
            public void connected(Connection connection) {
            }

            @Override
            public void received(Connection connection, Object message) {

                if (message instanceof LobbyJoined) {
                    screenToChangeTo = VoShooter.Screen.LOBBY;
                    joinLobby((LobbyJoined) message);
                } else if (message instanceof LobbyUserUpdate) {
                    updateLobby((LobbyUserUpdate) message);
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
                    playerDeathSet.add((PlayerDeath) message);
                } else if (message instanceof PlayerDead) {
                    updatePlayerDead((PlayerDead) message);
                } else if (message instanceof PlayerAmmoUpdate) {
                    handleSwitchWeapon((PlayerAmmoUpdate) message);
                } else if (message instanceof PlayerStatistics) {
                    updatePlayerStatistics((PlayerStatistics) message);
                } else if (message instanceof PlayerKothChange) {
                    updateKingOfTheHillAreaHolder((PlayerKothChange) message);
                } else if (message instanceof PlayerKothScores) {
                    updateKingOfTheHillStatistics((PlayerKothScores) message);
                } else if (message instanceof ChatReceiveMessage) {
                    receivedMessages.add((ChatReceiveMessage) message);
                } else if (message instanceof ChatGamePlayerChange) {
                    receivedPlayerChanges.add((ChatGamePlayerChange) message);
                } else if (message instanceof PlayerSwappedWeapon) {
                    handlePlayerWeaponUpdate((PlayerSwappedWeapon) message);
                } else if (message instanceof LobbySettingsChanged) {
                    updateLobbySettings((LobbySettingsChanged) message);
                } else if (message instanceof GameEnd) {
                    handleGameOver((GameEnd) message);
                    gameEnd = (GameEnd) message;
                    screenToChangeTo = VoShooter.Screen.END_SCREEN;
                } else if (message instanceof RailgunFired) {
                    railgunFiredSet.add((RailgunFired) message);
                } else if (message instanceof PlayerDashed) {
                    playerDashedMessages.add((PlayerDashed) message);
                }

                // Define actions to be taken on the next cycle
                // of the OpenGL rendering thread.
                Gdx.app.postRunnable(new Runnable() {
                    public void run() {
                        if (screenToChangeTo != null) {
                            if (screenToChangeTo != VoShooter.Screen.MAIN) parent.screen = null;
                            parent.changeScreen(screenToChangeTo);
                            if (screenToChangeTo == VoShooter.Screen.END_SCREEN) parent.endScreen.setStatistics(gameEnd);
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
                            createProjectiles(projectilesCreatedSet);
                            projectilesCreatedSet.clear();
                        }
                        if (!projectileDestroyedSet.isEmpty()) {
                            for (ProjectileDestroyed msg : projectileDestroyedSet) {
                                destroyProjectile(msg);
                                projectileDestroyedSet.remove(msg);
                            }
                        }
                        if (!playerDeathSet.isEmpty()) {
                            for (PlayerDeath msg : playerDeathSet) {
                                handlePlayerDeath(msg);
                                playerDeathSet.remove(msg);
                            }
                        }
                        if (!receivedMessages.isEmpty()) {
                            for (ChatReceiveMessage msg : receivedMessages) {
                                handleReceivedMessages(msg);
                                receivedMessages.remove(msg);
                            }
                        }
                        if (!receivedPlayerChanges.isEmpty()) {
                            for (ChatGamePlayerChange msg : receivedPlayerChanges) {
                                handleReceivedPlayerChanges(msg);
                                receivedPlayerChanges.remove(msg);
                            }
                        }
                        if (!railgunFiredSet.isEmpty()) {
                            for (RailgunFired msg : railgunFiredSet) {
                                handleRailgunFired(msg);
                                railgunFiredSet.remove(msg);
                            }
                        }
                        if (!playerDashedMessages.isEmpty()) {
                            for (PlayerDashed msg : playerDashedMessages) {
                                handlePLayerDash(msg);
                                playerDashedMessages.remove(msg);
                            }
                        }
                    }
                });
            }
        }));

        client.connect(MILLISECONDS_BEFORE_TIMEOUT, address, port);
    }

    private void updateKingOfTheHillStatistics(PlayerKothScores msg) {
        if (parent.gameState.userPlayer.clientGameModeManager instanceof ClientKingOfTheHillManager) {
            ((ClientKingOfTheHillManager) parent.gameState.userPlayer.clientGameModeManager).players = msg.players;
        }
    }

    private void handleGameOver(GameEnd msg) {
        parent.gameState.clearDrawables();
        parent.gameState.clearMessages();
        writeToFile(msg);
    }

    private void updateKingOfTheHillAreaHolder(PlayerKothChange msg) {
        // TODO add method.
    }

    /**
     * Join a lobby.
     * @param msg The message describing the information about the lobby.
     */
    private void joinLobby(LobbyJoined msg) {
            parent.gameState.currentLobby.handleJoining(msg);
    }

    private void updateLobbySettings(LobbySettingsChanged msg) {
        parent.gameState.currentLobby.setGamemode(msg.gameMode);
        parent.gameState.currentLobby.setMaxUsers(msg.maxUsers);
        parent.gameState.currentLobby.setMapType(msg.mapType);
        parent.gameState.currentLobby.setGameLength(msg.gameLength);
        parent.gameState.currentLobby.setBotAmount(msg.botAmount);
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
        if (parent.gameState.players.containsKey(msg.id)) {
            parent.gameState.players.get(msg.id).setPos(msg.pos);
        }
    }

    /**
     * Update the players' view directions.
     * @param msg The message describing the poses of the players.
     */
    private void updatePlayerViewDirections(PlayerViewUpdate msg) {
        if (parent.gameState.players.containsKey(msg.id)) {
            parent.gameState.players.get(msg.id).getSprite().setRotation(msg.viewDirection.angleDeg());
        }
    }

    /**
     * Update players' health.
     * @param msg The message containing info about player health.
     */
    private void updatePlayerHealth(PlayerHealthUpdate msg) {
        if (parent.gameState.players.containsKey(msg.id)) {
            parent.gameState.players.get(msg.id).setHealth(msg.health);
        }
    }

    /**
     * Update player respawn timer.
     * @param msg time until respawn.
     */
    private void updatePlayerDead(PlayerDead msg) {
        if (parent.gameState.players.containsKey(msg.id)) {
            parent.gameState.players.get(msg.id).respawnTimer = msg.timeToRespawn;
        }
    }

    /**
     * Message received when a player dies.
     * @param msg The message containing info about the death.
     */
    private void handlePlayerDeath(PlayerDeath msg) {
        parent.gameState.addDeathMessage(msg.playerId, msg.killerId, msg.weaponType);
        if (msg.playerId != msg.killerId) {
            if (msg.killerId == parent.gameState.userPlayer.getId()) {
                SoundPlayer.play("soundfx/ui/kill.ogg");
                parent.gameState.particleManager
                        .addParticleEffect(new Vector2(Gdx.graphics.getWidth(),
                                Gdx.graphics.getHeight() - MainScreen.KILLFEED_TOP_MARGIN - 18),
                        "particleeffects/ui/killfeedkill",
                        false,
                        true);
            } else if (msg.playerId == parent.gameState.userPlayer.getId()) {
                SoundPlayer.play("soundfx/ui/player_death.ogg");
                parent.gameState.particleManager
                        .addParticleEffect(new Vector2(Gdx.graphics.getWidth(),
                                Gdx.graphics.getHeight() - MainScreen.KILLFEED_TOP_MARGIN - 18),
                        "particleeffects/ui/killfeeddeath",
                        false,
                        true);
            }
        }
        ClientPlayer player = parent.gameState.players.get(msg.playerId);
        if (player != null && player.getPosition() != null) parent.gameState.particleManager
                .addParticleEffect(player.getPosition(),
                "particleeffects/player/playerdeath", false, false);
    }

    /**
     * Create new projectiles.
     * @param messages Projectile created messages.
     */
    private void createProjectiles(Set<ProjectileCreated> messages) {
        Set<Projectile.Type> projectileTypes = new HashSet<>();

        for (ProjectileCreated msg : messages) {
            if (!projectileTypes.contains(msg.type)) {
                SoundPlayer.play(ClientProjectile.getSoundCreatedPath(msg.type),
                        parent.gameState.userPlayer.getPosition(), msg.pos);
            }

            projectileTypes.add(msg.type);
            parent.gameState.createProjectile(msg);
            if (msg.type == Projectile.Type.ROCKET) {
                parent.gameState.particleManager.addParticleEffect(msg.pos, msg.pos.cpy().add(msg.vel),
                        "particleeffects/projectile/rocketfire");
            } else if (msg.type == Projectile.Type.GRENADE) {
                parent.gameState.particleManager.addParticleEffect(msg.pos, msg.pos.cpy().add(msg.vel),
                        "particleeffects/projectile/grenadelauncherfire");
            }
        }
    }

    /**
     * Update the positions of the projectiles.
     * @param msg Update message.
     */
    private void updateProjectilePositions(ProjectilePositions msg) {
        parent.gameState.updateProjectiles(msg);
    }

    /**
     * Destroy a projectile.
     * @param msg Projectile destroyed message.
     */
    private void destroyProjectile(ProjectileDestroyed msg) {
        ClientProjectile p = parent.gameState.getProjectiles().get((long) msg.id);
        if (p != null) {
            String path = ClientProjectile.getSoundDestroyedPath(p.getType());
            if (path != null) SoundPlayer.play(path, parent.gameState.userPlayer.getPosition(), p.getPosition());
            parent.gameState.destroyProjectile(msg);
        }
    }

    /**
     * Update players' statistics.
     * @param msg The message containing info about the player.
     */
    private void updatePlayerStatistics(PlayerStatistics msg) {
        MainScreen mainScreen = parent.mainScreen;
        if (mainScreen != null && mainScreen.clientGameModeManager != null) mainScreen.clientGameModeManager
                .setTimePassed(msg.timePassed);
        if (parent.gameState.players.containsKey(msg.id)) {
            ClientPlayer p = parent.gameState.players.get(msg.id);
            p.setKills(msg.kills);
            p.setDeaths(msg.deaths);
        }
    }

    /**
     * Switch weapons.
     * @param msg Update message.
     */
    private void handleSwitchWeapon(PlayerAmmoUpdate msg) {
        if (parent.gameState.userPlayer != null) {
            if (msg.weaponType != null) parent.gameState.userPlayer.setWeapon(msg.weaponType);
            parent.gameState.userPlayer.currentAmmo = msg.remainingAmmo;
        }
    }

    /**
     * Switch player weapon types.
     * @param msg
     */
    private void handlePlayerWeaponUpdate(PlayerSwappedWeapon msg) {
        if (parent.gameState.getPlayers().containsKey(msg.id)) {
            parent.gameState.getPlayers().get(msg.id).setWeapon(msg.weaponType);
        }
    }

    private void handlePLayerDash(PlayerDashed msg) {
        if (parent.gameState.getPlayers().containsKey(msg.id)) {
            ClientPlayer p = parent.gameState.getPlayers().get(msg.id);
            parent.gameState.particleManager.addParticleEffect(p.getPosition(),
                    p.getPosition().cpy().add(msg.direction),
                    "particleeffects/player/playerdash");
        }
    }

    /**
     * Handle railgun being fired. Create particles, play sound.
     * @param msg The railgun fired message.
     */
    private void handleRailgunFired(RailgunFired msg) {
        String path = "soundfx/gun/railgun.ogg";
        SoundPlayer.play(path, parent.gameState.userPlayer.getPosition(), msg.startPos);

        parent.gameState.particleManager
                .addParticleEffect(msg.startPos, msg.endPos, "particleeffects/projectile/railgun");
        parent.gameState.particleManager
                .addParticleEffect(msg.endPos, "particleeffects/projectile/railgunimpact", false, false);
    }

    /**
     * Receive a chat message.
     * @param msg The message.
     */
    private void handleReceivedMessages(ChatReceiveMessage msg) {
        ChatEntry entry = new ChatEntry();
        entry.setText(msg.message);
        entry.setBroadcast(false);
        if (parent.gameState.getPlayers().containsKey(msg.playerId)) {
            entry.setPrefix(parent.gameState.getPlayers().get(msg.playerId).getName());
        } else entry.setPrefix("unknown");
        parent.gameState.addChatEntry(entry);
    }

    /**
     * Receive a chat message.
     * @param msg The message.
     */
    private void handleReceivedPlayerChanges(ChatGamePlayerChange msg) {
        ChatEntry entry = new ChatEntry();
        entry.setText(msg.message);
        entry.setBroadcast(true);
        entry.setPrefix("Server");
        parent.gameState.addChatEntry(entry);
    }

    private void writeToFile(GameEnd msg) {
        String dir = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "VoShooter" + File.separator + "Games";
        final File file = new File(dir, (LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy__HH-mm-ss")) + ".txt"));
        file.getParentFile().mkdirs();
        try {
            System.out.println(file.getAbsolutePath());
            file.createNewFile();
            FileWriter fWriter = new FileWriter(file);
            fWriter.write(String.format("Time played: %s%nGamemode: %s%nMap: %s%nPlayer count: %s%nBot count: %s%n%n",
                   msg.gameLength, msg.gameMode, msg.mapType,
                    msg.playerCount, msg.botAmount));
            for (String line : msg.leaderBoard) {
                fWriter.write(line);
                fWriter.write("\n");
            }
            fWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

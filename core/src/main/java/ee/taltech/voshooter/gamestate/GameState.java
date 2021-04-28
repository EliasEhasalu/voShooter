package ee.taltech.voshooter.gamestate;

import ee.taltech.voshooter.entity.Entity;
import ee.taltech.voshooter.entity.clientprojectile.ClientProjectile;
import ee.taltech.voshooter.entity.player.ClientPlayer;
import ee.taltech.voshooter.networking.messages.User;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectileCreated;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectileDestroyed;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectilePositionUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectilePositions;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerAction;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.rendering.Drawable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GameState {

    private final Set<Entity> entities = new HashSet<>();
    private final Set<Drawable> drawableEntities = new HashSet<>();

    public ClientLobby currentLobby = new ClientLobby(this);
    public User clientUser = new User();
    public ClientPlayer userPlayer;
    public boolean ongoingGame = false;
    public List<PlayerAction> currentInputs = new ArrayList<>();

    public Map<Long, ClientPlayer> players = new ConcurrentHashMap<>();
    private final Map<Long, ClientProjectile> projectiles = new ConcurrentHashMap<>();
    public ParticleManager particleManager = new ParticleManager();

    public Queue<DeathMessage> deathMessages = new ArrayDeque<>();
    public Queue<ChatEntry> chatEntries = new ArrayDeque<>();
    public static final int MAX_CHAT_SIZE = 25;

    /** @return The list of drawable entities. */
    public Set<Drawable> getDrawables() {
        return drawableEntities;
    }

    /** @return The list of players. */
    public Map<Long, ClientPlayer> getPlayers() {
        return players;
    }

    /**
     * Act as a single game tick.
     */
    public void tick() {
       //.
    }

    /**
     * Add an entity to be tracked.
     * @param e The entity to be tracked.
     */
    public void addEntity(Entity e) {
        if (!entities.contains(e)) {
            entities.add(e);

            if (e instanceof Drawable) {
                drawableEntities.add((Drawable) e);
            }

            if (e instanceof ClientPlayer) {
                players.put(((ClientPlayer) e).getId(), (ClientPlayer) e);
            }
        }
    }

    /**
     * Update lobby entities.
     * @param players currently in lobby.
     */
    public void updatePlayers(List<Player> players) {
        for (ClientPlayer e : this.players.values()) {
            boolean userFound = false;
            for (Player p : players) {
                if (p.getId() == e.getId()) {
                    userFound = true;
                    break;
                }
            }
            if (!userFound) {
                entities.remove(e);
                this.players.remove(e.getId());
                drawableEntities.remove(e);
            }
        }
    }

    /**
     * Create all the player objects.
     * @param players List of players.
     */
    public void createPlayerObjects(List<Player> players) {
        for (Player p : players) {
            if (!getPlayers().containsKey(p.getId())) {
                ClientPlayer newP = new ClientPlayer(p.initialPos, p.getId(), p.getName());
                addEntity(newP);
                if (p.getId() == clientUser.id) {
                    userPlayer = newP;
                }
            }
        }
        ongoingGame = true;
    }

    /**
     * Create a new projectile.
     * @param msg Update message.
     */
    public void createProjectile(ProjectileCreated msg) {
        projectiles.put((long) msg.id, new ClientProjectile(msg));
    }

    /**
     * Destroy a projectile with a id.
     * @param msg Update message.
     */
    public void destroyProjectile(ProjectileDestroyed msg) {
        if (projectiles.containsKey((long) msg.id)) {
            ClientProjectile p = projectiles.get((long) msg.id);
            particleManager.addParticleEffect(p.getPosition(), p.getParticlePath(), false, false);
            projectiles.remove((long) msg.id);
        }
    }

    /**
     * Update the positions of the projectiles.
     * @param msg The update message.
     */
    public void updateProjectiles(ProjectilePositions msg) {
        for (ProjectilePositionUpdate u : msg.updates) {
            if (projectiles.containsKey((long) u.id)) {
                ClientProjectile p = projectiles.get((long) u.id);
                p.setPos(u.pos);
                p.setVel(u.vel);
            }
        }
    }

    /** Clear all drawable entities. */
    public void clearDrawables() {
        drawableEntities.clear();
        players.clear();
        entities.clear();
        projectiles.clear();
    }

    /** @return Set of projectiles on the client. */
    public Map<Long, ClientProjectile> getProjectiles() {
        return projectiles;
    }

    /**
     * Add a new death message.
     * @param playerId The player that died.
     * @param killerId The player that killed.
     */
    public void addDeathMessage(long playerId, long killerId) {
        ClientPlayer player = players.getOrDefault(playerId, null);
        ClientPlayer killer = players.getOrDefault(killerId, null);

        DeathMessage msg = new DeathMessage(player, killer);
        deathMessages.offer(msg);
    }

    /** Remove a message from the set of death messages. */
    public void removeDeathMessage() {
        deathMessages.poll();
    }

    /**
     * Add a chat entry.
     * @param entry The entry to be added.
     */
    public void addChatEntry(ChatEntry entry) {
        chatEntries.offer(entry);

        if (chatEntries.size() > MAX_CHAT_SIZE) removeChatEntry();
    }

    /** Remove the last chat entry. */
    public void removeChatEntry() {
        chatEntries.poll();
    }
}

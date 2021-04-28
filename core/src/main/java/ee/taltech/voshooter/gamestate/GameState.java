package ee.taltech.voshooter.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.AppPreferences;
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
    private final Set<ParticleEffect> particleEffects = ConcurrentHashMap.newKeySet();
    private final Set<ParticleEffect> uiParticles = ConcurrentHashMap.newKeySet();

    public Queue<DeathMessage> deathMessages = new ArrayDeque<>();
    public Queue<ChatEntry> chatEntries = new ArrayDeque<>();
    public static final int MAX_CHAT_SIZE = 20;

    /**
     * @return The list of drawable entities.
     */
    public Set<Drawable> getDrawables() {
        return drawableEntities;
    }

    /**
     * @return The list of players.
     */
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
            addParticleEffect(p.getPosition(), p.getParticlePath(), false, false);
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

    /**
     * Clear all drawable entities.
     */
    public void clearDrawables() {
        drawableEntities.clear();
        players.clear();
        entities.clear();
        projectiles.clear();
    }

    /**
     * @return Set of projectiles on the client.
     */
    public Map<Long, ClientProjectile> getProjectiles() {
        return projectiles;
    }

    /**
     * Add a new particle effect.
     * @param pos Position of the particle effect.
     * @param looping If the particle is looping or not.
     * @param path Path to the particle effect in assets.
     * @param isUI If the particle should be rendered on the UI.
     */
    public void addParticleEffect(Vector2 pos, String path, boolean looping, boolean isUI) {
        if (AppPreferences.getParticlesOn()) {
            ParticleEffect pe = new ParticleEffect();
            pe.load(Gdx.files.internal(path), Gdx.files.internal("textures/particles"));
            pe.setPosition(pos.x, pos.y);
            pe.start();

            if (isUI) {
                uiParticles.add(pe);
            } else {
                particleEffects.add(pe);
            }
        }
    }

    public void addParticleEffect(Vector2 pos, Vector2 endPos, String path) {
        if (AppPreferences.getParticlesOn()) {
            ParticleEffect pe = new ParticleEffect();
            pe.load(Gdx.files.internal(path), Gdx.files.internal("textures/particles"));
            ParticleEmitter em = pe.getEmitters().get(0);
            pe.setPosition(pos.x, pos.y);
            em.getSpawnHeight().setHigh(endPos.y - pos.y);
            em.getSpawnWidth().setHigh(endPos.x - pos.x);

            final float dist = new Vector2(0, 0).dst(new Vector2(em.getSpawnWidth().getHighMax(),
                    em.getSpawnHeight().getHighMax()));
            final float emission = em.getEmission().getHighMax() / dist * pos.dst(endPos);
            em.getEmission().setHigh(emission);
            em.setMaxParticleCount((int) emission);
            pe.start();

            particleEffects.add(pe);
        }
    }

    /**
     * Remove particle effects that have finished.
     * @param pe Particle effect to remove.
     */
    public void particleEffectFinished(ParticleEffect pe) {
        particleEffects.remove(pe);
        uiParticles.remove(pe);
        pe.dispose();
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

    /**
     * Remove a message from the set of death messages.
     */
    public void removeDeathMessage() {
        deathMessages.poll();
    }

    public void addChatEntry(ChatEntry entry) {
        chatEntries.offer(entry);

        if (chatEntries.size() > MAX_CHAT_SIZE) removeChatEntry();
    }

    public void removeChatEntry() {
        chatEntries.poll();
    }

    /** @return Set of particle effects currently in the game. */
    public Set<ParticleEffect> getParticleEffects() {
        return particleEffects;
    }

    /** @return Set of the UI particles. */
    public Set<ParticleEffect> getUiParticles() {
        return uiParticles;
    }
}

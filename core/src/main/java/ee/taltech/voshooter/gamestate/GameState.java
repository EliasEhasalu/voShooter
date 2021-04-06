package ee.taltech.voshooter.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.entity.Entity;
import ee.taltech.voshooter.entity.clientprojectile.ClientProjectile;
import ee.taltech.voshooter.entity.player.ClientPlayer;
import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.networking.messages.User;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectileCreated;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectileDestroyed;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectilePositionUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectilePositions;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerAction;
import ee.taltech.voshooter.rendering.Drawable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GameState {

    private final Set<Entity> entities = new HashSet<>();
    private final Set<Drawable> drawableEntities = new HashSet<>();

    public ClientLobby currentLobby = new ClientLobby(this);
    public User clientUser = new User();
    public ClientPlayer userPlayer;
    public List<PlayerAction> currentInputs = new ArrayList<>();

    public Set<ClientPlayer> players = ConcurrentHashMap.newKeySet();
    private final Set<ClientProjectile> projectiles = new HashSet<>();

    private Set<ParticleEffect> particleEffects = new HashSet<>();
    //private Set<ParticleEffect> loopingParticleEffects = new HashSet<>();

    /**
     * @return The list of drawable entities.
     */
    public Set<Drawable> getDrawables() {
        return drawableEntities;
    }

    /**
     * @return The list of players.
     */
    public Set<ClientPlayer> getPlayers() {
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
                players.add((ClientPlayer) e);
            }
        }
    }

    /**
     * Update lobby entities.
     * @param players currently in lobby.
     */
    public void updatePlayers(List<Player> players) {
        for (ClientPlayer e : this.players) {
            boolean userFound = false;
            for (Player p : players) {
                if (p.getId() == e.getId()) {
                    userFound = true;
                    break;
                }
            }
            if (!userFound) {
                entities.remove(e);
                this.players.remove(e);
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
            ClientPlayer newP = new ClientPlayer(p.initialPos, p.getId(), p.getName());
            addEntity(newP);
            if (p.getId() == clientUser.id) {
                userPlayer = newP;
            }
        }
    }

    /**
     * Create a new projectile.
     * @param msg Update message.
     */
    public void createProjectile(ProjectileCreated msg) {
        projectiles.add(new ClientProjectile(msg));
    }

    /**
     * Destroy a projectile with a id.
     * @param msg Update message.
     */
    public void destroyProjectile(ProjectileDestroyed msg) {
        for (ClientProjectile p : projectiles) {
            if (msg.id == p.getId()) {
                projectiles.remove(p);
            }
            addParticleEffect(p.getPosition(), false, "particleeffects/projectile/simpleexplosion");
        }
        //projectiles.removeIf(p -> p.getId() == msg.id);
    }

    /**
     * Update the positions of the projectiles.
     * @param msg The update message.
     */
    public void updateProjectiles(ProjectilePositions msg) {
        for (ProjectilePositionUpdate u : msg.updates) {
            for (ClientProjectile p : projectiles) {
                if (p.getId() == u.id) {
                    p.setPos(u.pos);
                    p.setVel(u.vel);
                    break;
                }
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
    public Set<ClientProjectile> getProjectiles() {
        return projectiles;
    }

    /**
     * Add a new particle effect.
     * @param pos Position of the particle effect.
     * @param looping If the particle is looping or not.
     * @param path Path to the particle effect in assets.
     */
    public void addParticleEffect(Vector2 pos, boolean looping, String path) {
        ParticleEffect pe = new ParticleEffect();
        pe.load(Gdx.files.internal(path), Gdx.files.internal(""));
        pe.setPosition(pos.x, pos.y);
        pe.start();

        particleEffects.add(pe);
    }

    /**
     * Remove particle effects that have finished.
     * @param pe Particle effect to remove.
     */
    public void particleEffectFinished(ParticleEffect pe) {
        particleEffects.remove(pe);
    }

    /** @return Set of particle effects currently in the game. */
    public Set<ParticleEffect> getParticleEffects() {
        return particleEffects;
    }

    ///** @return Particle effects that loop. */
    //public Set<ParticleEffect> getLoopingParticleEffects() {
    //    return loopingParticleEffects;
    //}
}

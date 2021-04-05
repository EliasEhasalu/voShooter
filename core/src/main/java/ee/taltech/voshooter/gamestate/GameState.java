package ee.taltech.voshooter.gamestate;

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

public class GameState {

    private final Set<Entity> entities = new HashSet<>();
    private final Set<Drawable> drawableEntities = new HashSet<>();

    public ClientLobby currentLobby = new ClientLobby(this);

    public User clientUser = new User();
    public ClientPlayer userPlayer;
    public List<PlayerAction> currentInputs = new ArrayList<>();

    public List<ClientPlayer> players = new ArrayList<>();
    private final Set<ClientProjectile> projectiles = new HashSet<>();

    /**
     * @return The list of drawable entities.
     */
    public Set<Drawable> getDrawables() {
        return drawableEntities;
    }

    /**
     * @return The list of players.
     */
    public List<ClientPlayer> getPlayers() {
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
        projectiles.removeIf(p -> p.getId() == msg.id);
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
    }

    /**
     * @return Set of projectiles on the client.
     */
    public Set<ClientProjectile> getProjectiles() {
        return projectiles;
    }
}

package ee.taltech.voshooter.networking.server.gamestate.entitymanager;

import com.badlogic.gdx.physics.box2d.World;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectileCreated;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectileDestroyed;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectilePositionUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectilePositions;
import ee.taltech.voshooter.networking.server.VoConnection;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.weapon.projectile.Projectile;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ProjectileManager extends EntityManager {

    private final Set<Projectile> projectiles = ConcurrentHashMap.newKeySet();
    private final Set<ProjectileCreated> createdProjectileUpdates = ConcurrentHashMap.newKeySet();
    private final Set<ProjectileDestroyed> destroyedProjectileUpdates = new HashSet<>();

    public ProjectileManager(World world, Game game) {
        super(world, game);
    }

    @Override
    public void update() {
        updateProjectiles();
        removeProjectiles();
        sendProjectileUpdates();
    }

    private void updateProjectiles() {
        projectiles.forEach(Projectile::update);
    }

    private void removeProjectiles() {
        projectiles.forEach(p -> {
            if (p.lifeTimeIsOver()) {
                destroyedProjectileUpdates.add(new ProjectileDestroyed(p.getId()));
                projectiles.remove(p);
            }
        });
    }

    @Override
    public void add(Object projectile) {
        Projectile p = (Projectile) projectile;

        projectiles.add(p);
        createdProjectileUpdates.add((ProjectileCreated) p.getUpdate());
    }

    private void sendProjectileUpdates() {
        ProjectilePositions update = new ProjectilePositions();

        update.updates = projectiles.stream()
                .map(p -> (ProjectilePositionUpdate) p.getUpdate())
                .collect(Collectors.toList());

        for (VoConnection c : game.getConnections()) {
            c.sendTCP(update);
            createdProjectileUpdates.forEach(c::sendTCP);
            destroyedProjectileUpdates.forEach(c::sendTCP);
        }

        createdProjectileUpdates.clear();
        destroyedProjectileUpdates.clear();
    }
}

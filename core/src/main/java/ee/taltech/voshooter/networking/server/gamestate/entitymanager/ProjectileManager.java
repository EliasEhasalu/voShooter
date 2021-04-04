package ee.taltech.voshooter.networking.server.gamestate.entitymanager;

import com.badlogic.gdx.physics.box2d.World;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectileCreated;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectilePositionUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectilePositions;
import ee.taltech.voshooter.networking.server.VoConnection;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.weapon.projectile.Projectile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjectileManager extends EntityManager {

    private final Set<Projectile> projectiles = new HashSet<>();

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
        projectiles.removeIf(Projectile::lifeTimeIsOver);
    }

    @Override
    public void add(Object p) {
        projectiles.add((Projectile) p);
    }

    private void sendProjectileUpdates() {
        ProjectilePositions update = new ProjectilePositions();
        List<ProjectilePositionUpdate> positions = new ArrayList<>();
        List<ProjectileCreated> newProjectiles = new ArrayList<>();

        for (Projectile projectile : projectiles) {
            Object up = projectile.getUpdate();
            if (up instanceof ProjectilePositionUpdate) {
                positions.add((ProjectilePositionUpdate) up);
            } else if (up instanceof ProjectileCreated) {
                newProjectiles.add((ProjectileCreated) up);
            }
        }
        update.updates = positions;

        for (VoConnection c : game.getConnections()) {
            c.sendTCP(update);
            newProjectiles.forEach(c::sendTCP);
        }
    }
}

package ee.taltech.voshooter.networking.server.gamestate.entitymanager;

import com.badlogic.gdx.physics.box2d.World;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.weapon.projectile.Projectile;

import java.util.HashMap;
import java.util.Map;

public class EntityManagerHub {

    private final World world;
    private final Game parent;
    private final Map<String, EntityManager> entityManagers = new HashMap<>();

    public EntityManagerHub(World world, Game parent) {
        this.world = world;
        this.parent = parent;

        this.entityManagers.put("Projectile", new ProjectileManager(world, parent));
        this.entityManagers.put("Player", new PlayerManager(world, parent));
    }

    public void add(Object p) {
        if (p instanceof Projectile) entityManagers.get("Projectile").add(p);
        if (p instanceof Player) entityManagers.get("Player").add(p);
    }

    public void update() {
        entityManagers.values().forEach(EntityManager::update);
    }

    public void sendUpdates() {
        entityManagers.values().forEach(EntityManager::sendUpdates);
    }
}

package ee.taltech.voshooter.networking.server.gamestate.entitymanager;

import com.badlogic.gdx.physics.box2d.World;
import ee.taltech.voshooter.networking.server.gamestate.Game;

public abstract class EntityManager {

    protected World world;
    protected Game game;

    public EntityManager(World world, Game game) {
        this.world = world;
        this.game = game;
    }

    public abstract void update();

    public abstract void add(Object p);
}

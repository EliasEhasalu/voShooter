package ee.taltech.voshooter.networking.server.gamestate.collision;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.weapon.projectile.Projectile;

public class CollisionHandler implements ContactListener {

    private final World world;

    public CollisionHandler(World world, Game parent) {
        this.world = world;
    }

    private void handleCustomCollisions(Contact c) {
        Fixture f1 = c.getFixtureA();
        Fixture f2 = c.getFixtureB();
        Object u1 = f1.getBody().getUserData();
        Object u2 = f2.getBody().getUserData();

        handleProjectileCollisions(u1, u2, f1, f2);
    }

    private void handleProjectileCollisions(Object u1, Object u2, Fixture f1, Fixture f2) {
        if (u1 instanceof Projectile) {
            ((Projectile) u1).setCollidedWith(f2);
        }
        if (u2 instanceof Projectile) {
            ((Projectile) u2).setCollidedWith(f1);
        }
    }

    @Override
    public void beginContact(Contact contact) {
        handleCustomCollisions(contact);
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}

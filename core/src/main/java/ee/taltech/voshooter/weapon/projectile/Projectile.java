package ee.taltech.voshooter.weapon.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectilePositionUpdate;
import ee.taltech.voshooter.networking.server.gamestate.collision.PixelToSimulation;
import ee.taltech.voshooter.networking.server.gamestate.collision.ShapeFactory;

public abstract class Projectile {

    private static int ID_GENERATOR = 0;

    protected int id;
    protected Body body;
    protected Player owner;
    protected Projectile.Type type;
    protected boolean isNew = true;
    protected Vector2 vel;

    public enum Type {
        ROCKET,
        PISTOL_BULLET
    }

    /**
     * Construct the projectile.
     * @param type The type of projectile.
     * @param owner The player who shot the projectile.
     * @param pos The position of the projectile.
     * @param vel The velocity of the projectile.
     */
    public Projectile(Projectile.Type type, Player owner, Vector2 pos, Vector2 vel) {
        this.vel = vel;
        this.type = type;
        this.owner = owner;
        this.id = ID_GENERATOR++;

        this.body = ShapeFactory.getRocket(owner.getGame().getWorld(), pos, vel);
        this.body.setUserData(this);  // Have the body remember this rocket object.
    }

    public abstract void handleCollision(Object o);

    /** @return The body object of the projectile. */
    public Body getBody() {
        return body;
    }

    /** @return Projectile type. */
    public Projectile.Type getType() {
        return type;
    }

    /** @return Projectile ID. */
    public int getId() {
        return id;
    }

    public ProjectilePositionUpdate getUpdate() {
        return new ProjectilePositionUpdate(
                getId(),
                PixelToSimulation.toPixels(body.getPosition()),
                PixelToSimulation.toPixels(vel)
        );
    }

    /** @return The position of the projectile in world space. */
    public Vector2 getPosition() {
       return body.getPosition().cpy();
    }
}

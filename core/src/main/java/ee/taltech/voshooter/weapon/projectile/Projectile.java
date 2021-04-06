package ee.taltech.voshooter.weapon.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectileCreated;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectilePositionUpdate;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.PixelToSimulation;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.ShapeFactory;

public abstract class Projectile {

    private static int ID_GENERATOR = 0;

    protected int id;
    private float lifeTime;
    private boolean isDestroyed = false;
    private Fixture collidedWith = null;

    protected Body body;
    protected Player owner;
    protected Projectile.Type type;
    protected boolean isNew = true;
    protected Vector2 vel;

    public enum Type {
        ROCKET,
        PISTOL_BULLET,
        FIREBALL
    }

    /**
     * Construct the projectile.
     * @param type The type of projectile.
     * @param owner The player who shot the projectile.
     * @param pos The position of the projectile.
     * @param vel The velocity of the projectile.
     */
    public Projectile(Projectile.Type type, Player owner, Vector2 pos, Vector2 vel, float lifeTime) {
        this.vel = vel;
        this.type = type;
        this.owner = owner;
        this.lifeTime = lifeTime;
        this.id = ID_GENERATOR++;

        this.body = ShapeFactory.getProjectileBody(type, owner.getGame().getWorld(), pos, vel);
        this.body.setUserData(this);  // Have the body remember this rocket object.
    }

    public abstract void handleCollision(Fixture fix);

    protected abstract void uponDestroy();

    public void destroy() {
        isDestroyed = true;
        uponDestroy();
    };

    public void update() {
        if (collidedWith != null) handleCollision(collidedWith);
        setCollidedWith(null);

        lifeTime -= (1 / Game.TICK_RATE_IN_HZ);
        if (lifeTimeIsOver()) destroy();
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public boolean lifeTimeIsOver() {
        return lifeTime <= 0;
    }

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

    public Object getUpdate() {
        if (isNew) {
            isNew = false;

            return new ProjectileCreated(
                    getType(),
                    PixelToSimulation.toPixels(getPosition()),
                    PixelToSimulation.toPixels(getVelocity()),
                    getId()
            );
        }

        return new ProjectilePositionUpdate(
                getId(),
                PixelToSimulation.toPixels(getPosition()),
                PixelToSimulation.toPixels(getVelocity())
        );
    }

    /** @return The position of the projectile in world space. */
    public Vector2 getPosition() {
       return body.getPosition().cpy();
    }

    private Vector2 getVelocity() {
        return body.getLinearVelocity().cpy();
    }

    public void setCollidedWith(Fixture fix) {
        collidedWith = fix;
    }

    protected void reduceLifeTime(float amount) {
        lifeTime -= amount;
    }
}

package ee.taltech.voshooter.networking.messages.clientreceived;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.weapon.projectile.Projectile;

public class ProjectileCreated {

    public Vector2 pos;
    public Vector2 vel;
    public Projectile.Type type;

    public ProjectileCreated() {
    }

    /**
     * Constructor.
     * @param type The type of projectile.
     * @param pos The position of the projectile.
     */
    public ProjectileCreated(Projectile.Type type, Vector2 pos, Vector2 vel) {
        this.type = type;
        this.pos = pos;
        this.vel = vel;
    }
}

package ee.taltech.voshooter.networking.messages.clientreceived;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.weapon.projectile.Projectile;

public class ProjectilePositionUpdate {

    public int id;
    public Projectile.Type type;
    public Vector2 pos;
    public Vector2 vel;
    public boolean isNew;

    /** Serialize. **/
    public ProjectilePositionUpdate() {
    }

    public ProjectilePositionUpdate(int id, Projectile.Type type, Vector2 pos, Vector2 vel, boolean isNew) {
        this.id = id;
        this.type = type;
        this.pos = pos;
        this.vel = vel;
        this.isNew = isNew;
    }
}

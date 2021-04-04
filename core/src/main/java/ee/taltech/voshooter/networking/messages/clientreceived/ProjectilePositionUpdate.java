package ee.taltech.voshooter.networking.messages.clientreceived;

import com.badlogic.gdx.math.Vector2;

public class ProjectilePositionUpdate {

    public int id;
    public Vector2 pos;
    public Vector2 vel;

    /** Serialize. **/
    public ProjectilePositionUpdate() {
    }

    public ProjectilePositionUpdate(int id, Vector2 pos, Vector2 vel) {
        this.id = id;
        this.pos = pos;
        this.vel = vel;
    }
}

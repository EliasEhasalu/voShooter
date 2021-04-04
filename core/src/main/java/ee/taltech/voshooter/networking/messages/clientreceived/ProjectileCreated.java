package ee.taltech.voshooter.networking.messages.clientreceived;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class ProjectileCreated {

    private final Sprite sprite;
    private final Vector2 pos;

    /**
     * Constructor.
     * @param sprite The sprite of the projectile.
     * @param pos The position of the projectile.
     */
    public ProjectileCreated(Sprite sprite, Vector2 pos) {
        this.sprite = sprite;
        this.pos = pos;
    }

    /** @return Sprite of the projectile. */
    public Sprite getSprite() {
        return sprite;
    }

    /** @return The position of the projectile in pixel space. */
    public Vector2 getPos() {
        return pos;
    }
}

package ee.taltech.voshooter.networking.messages.clientreceived;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class ProjectileUpdate {

    public Sprite sprite;
    public Vector2 pos;

    public ProjectileUpdate() {
    }

    /**
     * Constructor.
     * @param sprite The sprite of the projectile to update.
     * @param pos New position.
     */
    public ProjectileUpdate(Sprite sprite, Vector2 pos) {
        this.sprite = sprite;
        this.pos = pos;
    }

    /**
     * @return The sprite to update the position of.
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * @return The new position of the projectile.
     */
    public Vector2 getPos() {
        return pos;
    }
}

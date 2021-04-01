package ee.taltech.voshooter.networking.messages.clientreceived;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class ProjectileDestroyed {

    private final Sprite sprite;

    /**
     * Constructor.
     * @param sprite The sprite to destroy.
     */
    public ProjectileDestroyed(Sprite sprite) {
        this.sprite = sprite;
    }

    /**
     * @return The sprite to destroy.
     */
    public Sprite getSprite() {
        return sprite;
    }
}

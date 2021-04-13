package ee.taltech.voshooter.rendering;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public interface Drawable {

    /**
     * @return The sprite of this drawable object.
     */
    Sprite getSprite();

    /**
     * @return Whether the sprite is currently visible.
     */
    boolean isVisible();

    /**
     * @return A position object describing where this drawable object's sprite should be drawn.
     */
    Vector2 getPosition();

    /**
     * @return What the scale of the drawable's sprite should currently be.
     */
    float getScale();

    /**
     * @return amount of kills a drawable has, min_integer if it can't have kills.
     */
    int getKills();
}

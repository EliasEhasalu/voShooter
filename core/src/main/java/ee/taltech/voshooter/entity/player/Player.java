package ee.taltech.voshooter.entity.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ee.taltech.voshooter.entity.Entity;
import ee.taltech.voshooter.geometry.Pos;
import ee.taltech.voshooter.rendering.Drawable;

public class Player extends Entity implements Drawable {

    private Sprite sprite;
    private float spriteScale = -0.85f;

    /**
     * Construct player with default sprite.
     * @param position The initial position of this player.
     */
    public Player(Pos position) {
        super(position);
        this.sprite = new Sprite(new Texture("proxy-image.png"));
        this.sprite.scale(spriteScale);
    }

    /**
     * Construct player with specific sprite.
     * @param position The initial position of this player.
     * @param spritePath The path to the sprite image this player should have.
     */
    public Player(Pos position, String spritePath) {
        super(position);
        this.sprite = new Sprite(new Texture(spritePath));
        this.sprite.scale(spriteScale);
    }

    /**
     * @return This player object's sprite.
     */
    @Override
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * @return What the scale of this player's sprite should currently be.
     */
    @Override
    public float getScale() {
        return spriteScale;
    }

    /**
     * @return Whether the player's sprite should be visible at the moment.
     */
    @Override
    public boolean isVisible() {
        return true;
    }
}

package ee.taltech.voshooter.entity.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ee.taltech.voshooter.entity.Entity;
import ee.taltech.voshooter.geometry.Pos;
import ee.taltech.voshooter.rendering.Drawable;

public class ClientPlayer extends Entity implements Drawable {

    private Sprite sprite;
    private float spriteScale = -0.85f;
    private long id;
    private String name;

    /**
     * Construct player with default sprite.
     * @param position The initial position of this player.
     */
    public ClientPlayer(Pos position, long id, String name) {
        super(position);
        this.id = id;
        this.sprite = new Sprite(new Texture("proxy-image.png"));
        this.sprite.scale(spriteScale);
    }

    /**
     * Construct player with specific sprite.
     * @param position The initial position of this player.
     * @param spritePath The path to the sprite image this player should have.
     */
    public ClientPlayer(Pos position, String spritePath) {
        super(position);
        this.sprite = new Sprite(new Texture(spritePath));
        this.sprite.scale(spriteScale);
        this.sprite.setCenterX(position.getX());
        this.sprite.setCenterY(position.getY());
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

    /**
     * @return Get the player's id.
     */
    public long getId() {
        return id;
    }

    /**
     * Set the player's position.
     * @param pos The position to set the player to.
     */
    public void setPos(Pos pos) {
        this.position = pos;
        this.sprite.setCenterX(pos.getX());
        this.sprite.setCenterY(pos.getY());
    }

    /**
     * @return The player's name.
     */
    public String getName() {
        return name;
    }
}

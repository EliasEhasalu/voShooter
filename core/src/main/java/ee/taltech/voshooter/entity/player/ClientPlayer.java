package ee.taltech.voshooter.entity.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.entity.Entity;
import ee.taltech.voshooter.rendering.Drawable;

public class ClientPlayer extends Entity implements Drawable {

    public static final int MAX_HEALTH = 100;
    private Sprite sprite;
    private Integer health;
    private float spriteScale = -0.85f;
    private long id;
    private String name;

    /**
     * Construct player with specific sprite.
     * @param position The initial position of this player.
     * @param spritePath The path to the sprite image this player should have.
     */
    public ClientPlayer(Vector2 position, long id, String name, String spritePath) {
        super(position);
        this.id = id;
        this.name = name;
        this.health = MAX_HEALTH;
        this.sprite = new Sprite(new Texture(spritePath));
        this.sprite.scale(spriteScale);
    }

    /**
     * Construct player with specific sprite.
     * @param position The initial position of this player.
     * @param id .
     * @param name .
     */
    public ClientPlayer(Vector2 position, long id, String name) {
        super(position);
        this.id = id;
        this.name = name;
        this.health = MAX_HEALTH;
        this.sprite = new Sprite(new Texture("proxy-image.png"));
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
        return (health > 0);
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
    public void setPos(Vector2 pos) {
        this.position = pos;
        this.sprite.setCenterX(pos.x);
        this.sprite.setCenterY(pos.y);
    }

    /**
     * @return Get the amount of health the player has.
     */
    public Integer getHealth() {
        return health;
    }

    /**
     * @param amount Decrease the player health by amount.
     */
    public void setHealth(Integer amount) {
        this.health = amount;
    }

    /**
     * @return The player's name.
     */
    public String getName() {
        return name;
    }
}

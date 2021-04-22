package ee.taltech.voshooter.entity.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.entity.Entity;
import ee.taltech.voshooter.gamestate.gamemode.ClientGameModeManager;
import ee.taltech.voshooter.rendering.Drawable;
import ee.taltech.voshooter.weapon.Weapon;

public class ClientPlayer extends Entity implements Drawable {

    public static final int MAX_HEALTH = 100;
    private Sprite sprite;
    private Integer health;
    private float spriteScale = -0.85f;
    private long id;
    private String name;
    private int deaths;
    private int kills;
    public float respawnTimer;
    private Weapon.Type weapon;
    public int currentAmmo = 0;
    public int maxAmmo = 0;
    public ClientGameModeManager clientGameModeManager;
    private Weapon.Type heldWeapon = Weapon.Type.PISTOL;

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
        this.sprite = new Sprite(new Texture("textures/player/player.png"));
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

    /**
     * @return amount of deaths.
     */
    public int getDeaths() {
        return deaths;
    }

    /**
     * @param deaths amount of deaths.
     */
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    /**
     * @return amount of kills.
     */
    public int getKills() {
        return kills;
    }

    /**
     * @param kills amount of kills.
     */
    public void setKills(int kills) {
        this.kills = kills;
    }

    /**
     * @param weapon The weapon to change to.
     */
    public void setWeapon(Weapon.Type weapon) {
        this.weapon = weapon;
    }

    public Weapon.Type getWeapon() {
        return weapon;
    }

    public Weapon.Type getHeldWeapon() {
        return heldWeapon;
    }

    public void setHeldWeapon(Weapon.Type heldWeapon) {
        this.heldWeapon = heldWeapon;
    }
}

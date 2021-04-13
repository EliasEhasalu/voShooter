package ee.taltech.voshooter.networking.server.gamestate.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.entitymanager.PlayerManager;
import ee.taltech.voshooter.weapon.Weapon;
import ee.taltech.voshooter.weapon.projectileweapon.Pistol;

public class Player {

    public static final Integer MAX_HEALTH = 100;
    private static final float RESPAWN_TIME = 5f;

    private long id;
    private String name;
    private Integer health;
    public Vector2 initialPos;
    private float respawnTime = 5f;
    public boolean deathTick = false;
    private long killerId;
    private int deaths;
    private int kills;

    private transient Body body;
    private transient Weapon currentWeapon = new Pistol(this);
    private final transient PlayerStatusManager statusManager = new PlayerStatusManager(this);
    private transient PlayerManager playerManager;

    private final Vector2 playerAcc = new Vector2(0f, 0f);
    private Vector2 viewDirection = new Vector2(0f, 0f);

    /** Serialize. **/
    public Player() {
    }

    /**
     * @param id The ID associated with the player.
     * @param name The name associated with the player.
     */
    public Player(PlayerManager playerManager, long id, String name) {
        this.id = id;
        this.name = name;
        this.health = MAX_HEALTH;

        this.playerManager = playerManager;
    }

    /**
     * Set the view direction of the player.
     * @param update The update to base the view direction on.
     */
    public void setViewDirection(MouseCoords update) {
       viewDirection = new Vector2(update.x, update.y);
    }

    /**
     * Add a velocity vector based on player inputs to this player.
     * Is capped to a max movement speed.
     * @param xDir The direction to move on the x-axis.
     * @param yDir The direction to move on the y-axis.
     */
    public void addMoveDirection(int xDir, int yDir) {
        float basePlayerAcceleration = (float) (800f / Game.TICK_RATE_IN_HZ);
        Vector2 moveVector = new Vector2(basePlayerAcceleration * xDir, basePlayerAcceleration * yDir);
        playerAcc.add(moveVector);
        playerAcc.limit(basePlayerAcceleration);
    }

    /**
     * Update the player.
     */
    public void update() {
        if (health <= 0) respawn();
        statusManager.update();
        currentWeapon.coolDown();
        move();
    }

    /**
     * Update the player's position.
     */
    private void move() {
        float maxPlayerVelocity = 10f;

        body.applyLinearImpulse(playerAcc, body.getPosition(), true);

        if (body.getLinearVelocity().len() > maxPlayerVelocity) {
            body.setLinearVelocity(body.getLinearVelocity().cpy().limit(maxPlayerVelocity));
        }
        playerAcc.limit(0);  // Reset player acceleration vector after application.
    }

    /**
     * Shoot the current weapon.
     */
    public void shoot() {
       currentWeapon.fire();
    }

    /**
     * Take damage from bullets or other things.
     * @param amount of damage to take.
     */
    public void takeDamage(int amount) {
        if (health > 0) {
            health -= amount;
            if (health <= 0) {
                deathTick = true;
                deaths++;
            }
        }
    }

    /**
     * Respawn the player.
     */
    public void respawn() {
        if (respawnTime <= 0) {
            health = MAX_HEALTH;
            body.setTransform(getSpawnPoint(), 0f);
            respawnTime = RESPAWN_TIME;
        } else {
            respawnTime -= (1 / Game.TICK_RATE_IN_HZ);
        }
    }

    /**
     * @return A string representation.
     */
    public String toString() {
        return String.format("(%f, %f)", getPos().x, getPos().y);
    }

    /**
     * @return The position of this player.
     */
    public Vector2 getPos() {
        return body.getPosition();
    }

    /**
     * @return The view direction of this player.
     */
    public Vector2 getViewDirection() {
        return viewDirection;
    }

    /**
     * @return The id associated with this player.
     */
    public long getId() {
        return id;
    }

    /**
     * @return This player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return This player's health.
     */
    public Integer getHealth() {
        return health;
    }

    /**
     * @param b The body that
     */
    public void setBody(Body b) {
        this.body = b;
    }

    public Body getBody() {
        return body;
    }

    /**
     * @return the amount of times player has died.
     */
    public int getDeaths() {
        return deaths;
    }

    private Vector2 getSpawnPoint() {
        return playerManager.getSpawnPoint();
    }

    /**
     * @return the amount of times this player has killed.
     */
    public int getKills() {
        return kills;
    }

    /**
     * Add a kill for this person.
     */
    public void addKill() {
        this.kills++;
    }

    /**
     * Remove a kill from this person.
     */
    public void removeKill() {
        this.kills--;
    }

    /**
     * @return how much time is left until respawn.
     */
    public float getRespawnTime() {
        return respawnTime;
    }

    /**
     * Set killerId to be the id of the player that killed this player.
     * @param id Killer id.
     */
    public void setKillerId(long id) {
        killerId = id;
    }

    /** @return The id of the player that killed this player. */
    public long getKillerId() {
        return killerId;
    }

    /**
     * @param weapon to give the player.
     */
    public void setWeapon(Weapon weapon) {
        currentWeapon = weapon;
    }

    /**
     * @return get the current weapon of the player.
     */
    public Weapon getWeapon() {
        return currentWeapon;
    }

    public World getWorld() {
        return playerManager.getWorld();
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public boolean isAlive() {
        return (health > 0);
    }

    public Game getGame() {
        return playerManager.getGame();
    }
}

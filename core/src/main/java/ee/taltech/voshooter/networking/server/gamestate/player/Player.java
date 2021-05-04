package ee.taltech.voshooter.networking.server.gamestate.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerDashed;
import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.server.VoConnection;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.entitymanager.PlayerManager;
import ee.taltech.voshooter.networking.server.gamestate.player.status.Burning;
import ee.taltech.voshooter.networking.server.gamestate.player.status.DamageDealer;
import ee.taltech.voshooter.networking.server.gamestate.player.status.PlayerStatusManager;
import ee.taltech.voshooter.networking.server.gamestate.statistics.StatisticsTracker;
import ee.taltech.voshooter.weapon.Weapon;
import ee.taltech.voshooter.weapon.projectile.Fireball;

import static java.lang.Math.max;

public class Player {

    public static final Integer MAX_HEALTH = 100;
    private static final float RESPAWN_TIME = 5f;
    private static final float DASH_FORCE = 200f;

    private long id;
    private String name;
    private Integer health;
    public Vector2 initialPos;
    private float respawnTime = 5f;

    private transient VoConnection connection;
    private transient Body body;
    private transient Fixture fixture;
    private transient PlayerManager playerManager;
    private final transient PlayerStatusManager statusManager = new PlayerStatusManager(this);
    private final transient Inventory inventory = new Inventory(this);

    private final Vector2 playerAcc = new Vector2(0f, 0f);
    private Vector2 viewDirection = new Vector2(0f, 0f);
    private float maxPlayerVelocity = 10f;
    private static final float REGULAR_MAX_PLAYER_VELOCITY = 10f;

    /** Serialize. **/
    public Player() {
    }

    /**
     * @param id The ID associated with the player.
     * @param name The name associated with the player.
     */
    public Player(PlayerManager playerManager, VoConnection connection, long id, String name) {
        this.id = id;
        this.name = name;
        this.health = MAX_HEALTH;

        this.connection = connection;
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
        if (!(isAlive())) respawn();
        statusManager.update();
        inventory.update();
        move();
    }

    public void dash() {
        if (statusManager.canDash()) {
            maxPlayerVelocity = 30f;
            if (body.getLinearVelocity().len() <= 0.1f) {
                body.applyLinearImpulse(getViewDirection().cpy().nor().setLength(DASH_FORCE), body.getPosition(), true);
            } else {
                body.applyLinearImpulse(body.getLinearVelocity().cpy().nor().setLength(DASH_FORCE), body.getPosition(), true);
            }
            statusManager.playerDashed();

            for (VoConnection c : getGame().getConnections()) {
                c.sendTCP(new PlayerDashed(id, body.getLinearVelocity()));
            }
        }
    }

    /**
     * Update the player's position.
     */
    private void move() {
        body.applyLinearImpulse(playerAcc, body.getPosition(), true);

        if (body.getLinearVelocity().len() > maxPlayerVelocity) {
            body.setLinearVelocity(body.getLinearVelocity().cpy().limit(maxPlayerVelocity));
        }
        playerAcc.limit(0);  // Reset player acceleration vector after application.
        maxPlayerVelocity = max(REGULAR_MAX_PLAYER_VELOCITY, maxPlayerVelocity - 1f);
    }

    /**
     * Shoot the current weapon.
     */
    public void shoot() {
       inventory.attemptToFireCurrentWeapon();
    }

    /**
     * Take damage from bullets or other things.
     * @param amount of damage to take.
     */
    public void takeDamage(int amount) {
        if (health > 0) {
            health -= amount;
            if (health <= 0) die();
        }
    }

    public void takeDamage(int amount, DamageDealer source) {
        if (source instanceof Fireball) statusManager.applyDebuff(new Burning(this, source.getDamageSource()));
        getStatisticsTracker().setLastDamageTakenFrom(this, source, amount);

        takeDamage(amount);
    }

    public void purge() {
        getWorld().destroyBody(body);
        body = null;
        fixture = null;
    }

    private void die() {
        getStatisticsTracker().incrementDeaths(this);
        fixture.setSensor(true);
    }

    /** Respawn the player. */
    public void respawn() {
        if (respawnTime <= 0) {
            health = MAX_HEALTH;
            statusManager.resetCoolDowns();
            fixture.setSensor(false);
            body.setTransform(getSpawnPoint(), 0f);
            respawnTime = RESPAWN_TIME;
        } else {
            respawnTime -= (1 / Game.TICK_RATE_IN_HZ);
        }
    }

    /** @return A string representation. */
    public String toString() {
        return String.format("(%f, %f)", getPos().x, getPos().y);
    }

    /** @return The position of this player. */
    public Vector2 getPos() {
        return body.getPosition();
    }

    /** @return The view direction of this player. */
    public Vector2 getViewDirection() {
        return viewDirection;
    }

    /** @return The id associated with this player. */
    public long getId() {
        return id;
    }

    /** @return This player's name. */
    public String getName() {
        return name;
    }

    /** @return This player's health. */
    public Integer getHealth() {
        return health;
    }

    /** @param b The body that. */
    public void setBody(Body b) {
        this.body = b;
    }

    public void setFixture(Fixture f) {
        this.fixture = f;
    }

    public Body getBody() {
        return body;
    }

    private Vector2 getSpawnPoint() {
        return playerManager.getSpawnPoint();
    }

    /**
     * @param weaponType to give the player.
     */
    public void setWeapon(Weapon.Type weaponType) {
        inventory.swapToWeapon(weaponType);
    }

    /**
     * @return get the current weapon of the player.
     */
    public Weapon getWeapon() {
        return inventory.getCurrentWeapon();
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

    private StatisticsTracker getStatisticsTracker() {
        return getGame().getStatisticsTracker();
    }

    public VoConnection getConnection() {
        return connection;
    }

    public float getTimeToRespawn() {
        return respawnTime;
    }

    public PlayerStatusManager getStatusManager() {
        return statusManager;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void increaseMaxVelocity(float increase) {
        maxPlayerVelocity += increase;
    }

    public boolean isBot() {
        return (connection == null);
    }
}

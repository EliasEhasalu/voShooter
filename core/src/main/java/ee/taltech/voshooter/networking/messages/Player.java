package ee.taltech.voshooter.networking.messages;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.weapon.RocketLauncher;
import ee.taltech.voshooter.weapon.Weapon;

public class Player {

    private transient float basePlayerAcceleration = (float) (100f / Game.TICK_RATE_IN_HZ);
    private final transient float MAX_PLAYER_VELOCITY = 5000f;

    private transient Game game;

    private long id;
    private String name;
    public Vector2 initialPos;

    private transient Body body;
    private transient Weapon currentWeapon = new RocketLauncher(this);

    private final Vector2 playerAcc = new Vector2(0f, 0f);
    private Vector2 viewDirection = new Vector2(0f, 0f);

    /** Serialize. **/
    public Player() {
    }

    /**
     * @param id The ID associated with the player.
     * @param name The name associated with the player.
     */
    public Player(Game game, long id, String name) {
        this.game = game;
        this.id = id;
        this.name = name;
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
        Vector2 moveVector = new Vector2(basePlayerAcceleration * xDir, basePlayerAcceleration * yDir);
        playerAcc.add(moveVector);
        playerAcc.limit(basePlayerAcceleration);
    }

    public void update() {
        currentWeapon.coolDown();
        move();
        if (id == 0) System.out.println(body.getLinearVelocity());
    }

    /**
     * Update the player's position.
     */
    private void move() {
        body.applyForceToCenter(playerAcc, true);
        if (body.getLinearVelocity().len() > MAX_PLAYER_VELOCITY) {
            body.getLinearVelocity().limit(MAX_PLAYER_VELOCITY);
        }
        playerAcc.limit(0);  // Reset player acceleration vector after application.
    }

    /**
     * Shoot the current weapon.
     */
    public void shoot() {
        if (currentWeapon.canFire()) currentWeapon.fire();
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
     * @param b The body that
     */
    public void setBody(Body b) {
        this.body = b;
    }

    public Game getGame() {
        return game;
    }

    public Body getBody() {
        return body;
    }
}

package ee.taltech.voshooter.networking.messages;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.geometry.Pos;
import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.server.gamestate.Draggable;
import ee.taltech.voshooter.networking.server.gamestate.Game;

public class Player implements Draggable {

    private static final transient float BASE_PLAYER_ACCELERATION = (float) (35.0f / Game.TICK_RATE_IN_HZ);
    private static final transient float MAX_PLAYER_VELOCITY = (float) (300.0f / Game.TICK_RATE_IN_HZ);

    private long id;

    private String name;

    private Pos pos;

    private final Vector2 vel = new Vector2(0f, 0f);
    private Vector2 viewDirection = new Vector2(0f, 0f);

    private final Vector2 playerMovement = new Vector2(0f, 0f);

    /** Serialize. **/
    public Player() {
    }

    /**
     * @param initialPos The starting position of the player.
     * @param id The ID associated with the player.
     * @param name The name associated with the player.
     */
    public Player(Pos initialPos, long id, String name) {
        this.pos = initialPos;
        this.id = id;
        this.name = name;
    }

    /**
     * Set the view direction of the player.
     * @param update The update to base the view direction on.
     */
    public void setViewDirection(MouseCoords update) {
       viewDirection = new Vector2(update.x - pos.getX(), update.y - pos.getY());
    }

    /**
     * Add a velocity vector based on player inputs to this player.
     * Is capped to a max movement speed.
     * @param xDir The direction to move on the x-axis.
     * @param yDir The direction to move on the y-axis.
     */
    public void addMoveDirection(int xDir, int yDir) {
        Vector2 moveVector = new Vector2(BASE_PLAYER_ACCELERATION * xDir, BASE_PLAYER_ACCELERATION * yDir);
        playerMovement.add(moveVector);
        playerMovement.limit(BASE_PLAYER_ACCELERATION);
    }

    /**
     * Add a force to this player's velocity vector.
     * @param force The force to add.
     */
    public void addForce(Vector2 force) {
        vel.add(force);
    }

    /**
     * Update the player's position.
     */
    public void move() {
        // Add the player movement vector to player's velocity, and then clear it.
        vel.add(playerMovement);
        playerMovement.limit(0f);

        // Move the player.
        vel.limit(MAX_PLAYER_VELOCITY);
        pos.add(vel);
    }

    /**
     * Shoot the current weapon.
     */
    public void shoot() {
       // TODO
    }

    /**
     * @param dragFactor The factor to reduce this entity's velocity vector by.
     */
    @Override
    public void drag(float dragFactor) {
        this.vel.x *= dragFactor;
        this.vel.y *= dragFactor;

        // Round off to 0 if already very small.
        if (Math.abs(vel.x) < 0.1) vel.x = 0;
        if (Math.abs(vel.y) < 0.1) vel.y = 0;
    }

    /**
     * @return A string representation.
     */
    public String toString() {
        return String.format("(%f, %f)", this.pos.getX(), this.pos.getY());
    }

    /**
     * @return The position of this player.
     */
    public Pos getPos() {
        return pos;
    }

    /**
     * @return The velocity vector of this player.
     */
    public Vector2 getVel() {
        return vel;
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
}

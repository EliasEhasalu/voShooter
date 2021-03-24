package ee.taltech.voshooter.networking.messages;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.server.gamestate.Draggable;
import ee.taltech.voshooter.networking.server.gamestate.Game;

public class Player implements Draggable {

    private static final transient float BASE_PLAYER_ACCELERATION = (float) (0.1f / Game.TICK_RATE_IN_HZ);

    private long id;
    private String name;
    public Vector2 initialPos;

    private transient Body body;

    private final Vector2 playerAcc = new Vector2(0f, 0f);
    private Vector2 viewDirection = new Vector2(0f, 0f);

    /** Serialize. **/
    public Player() {
    }

    /**
     * @param id The ID associated with the player.
     * @param name The name associated with the player.
     */
    public Player(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Set the view direction of the player.
     * @param update The update to base the view direction on.
     */
    public void setViewDirection(MouseCoords update) {
       viewDirection = new Vector2(update.x - getPos().x, update.y - getPos().y);
    }

    /**
     * Add a velocity vector based on player inputs to this player.
     * Is capped to a max movement speed.
     * @param xDir The direction to move on the x-axis.
     * @param yDir The direction to move on the y-axis.
     */
    public void addMoveDirection(int xDir, int yDir) {
        Vector2 moveVector = new Vector2(BASE_PLAYER_ACCELERATION * xDir, BASE_PLAYER_ACCELERATION * yDir);
        playerAcc.add(moveVector);
        playerAcc.limit(BASE_PLAYER_ACCELERATION);
    }

    /**
     * Update the player's position.
     */
    public void move() {
        body.applyLinearImpulse(playerAcc, body.getPosition(), true);
        playerAcc.limit(0);  // Reset player acceleration vector after application.
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
        body.setLinearVelocity(body.getLinearVelocity().scl(dragFactor));
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
}

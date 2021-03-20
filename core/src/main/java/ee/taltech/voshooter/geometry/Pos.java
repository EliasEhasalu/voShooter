package ee.taltech.voshooter.geometry;

import com.badlogic.gdx.math.Vector2;

public class Pos {

    private float x;
    private float y;

    /** Serialize **/
    public Pos() {
    }

    /**
     * Construct a position object.
     * @param x The x coordinate of the position.
     * @param y The y coordinate of the position.
     */
    public Pos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return This position's x coordinate.
     */
    public float getX() {
        return x;
    }

    /**
     * @return This position's y coordinate.
     */
    public float getY() {
        return y;
    }

    /**
     * Update this position's x coordinate.
     * @param inc The amount to update by.
     */
    public void addX(float inc) {
       x += inc;
    }

    /**
     * Update this position's y coordinate.
     * @param inc The amount to update by.
     */
    public void addY(float inc) {
        y += inc;
    }

    /**
     * @param vec The vector to add to this position.
     */
    public void add(Vector2 vec) {
        x += vec.x;
        y += vec.y;
    }

    /**
     * @return The distance between this position and the given position.
     * @param that The given position.
     */
    public float distanceTo(Pos that) {
        return (float) (Math.sqrt(Math.pow((that.getX() - this.getX()), 2) + Math.pow((that.getY() - this.getY()), 2)));
    }

    /**
     * Check if the other object is a Position object with the same coordinates.
     * @param o The other object.
     * @return Whether the two objects are equal.
     */
    @SuppressWarnings("checkstyle:EqualsHashCode")
    public boolean equals(Object o) {
        if (!(o instanceof Pos)) return false;
        return (((Pos) o).x == this.x && ((Pos) o).y == this.y);
    }
}

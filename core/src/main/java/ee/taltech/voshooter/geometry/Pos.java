package ee.taltech.voshooter.geometry;

public class Pos {

    private float x;
    private float y;

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
     * @return The distance between this position and the given position.
     * @param that The given position.
     */
    public float distanceTo(Pos that) {
        return (float) (Math.sqrt(Math.pow((that.getX() - this.getX()), 2) + Math.pow((that.getY() - this.getY()), 2)));
    }
}

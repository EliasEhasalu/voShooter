package ee.taltech.voshooter.networking.messages.serverreceived;

public class MouseCoords {

    public float x;
    public float y;

    /** */
    public MouseCoords() { }

    /**
     * Construct the message.
     * @param x coordinate of the mouse.
     * @param y coordinate of the mouse.
     */
    public MouseCoords(float x, float y) {
        this.x = x;
        this.y = y;
    }
}

package ee.taltech.voshooter.networking.server.gamestate;

public interface Draggable {

    /**
     * Reduce this entity's velocity vector.
     * @param dragFactor The factor to reduce the velocity by.
     */
    void drag(float dragFactor);
}

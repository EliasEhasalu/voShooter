package ee.taltech.voshooter.geometry;

public interface Movable {

    /**
     * @param newPos The position to set this Movable to.
     * @return Whether the move was successful.
     */
    boolean setPos(Pos newPos);
}

package ee.taltech.voshooter.networking.messages.serverreceived;

public class PlayerAction {

    /** Serialize. **/
    public PlayerAction() {
    }

    /**
     * Make sure messages of the same class are equal.
     * @param o The other object to compare.
     * @return Whether the messages are equal.
     */
    @Override
    public boolean equals(Object o) {
        return o.getClass() == getClass();
    }

    /**
     * @return A hash.
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

package ee.taltech.voshooter.networking.messages;

public class User {
    private String name;
    private boolean isHost = false;

    /** @return The name of this user. */
    public String getName() {
        return (name == null) ? "Anonymous" : name;
    }

    /**
     * @return If the user is the host for a lobby.
     */
    public boolean getIsHost() {
        return isHost;
    }

    /**
     * @param isHost Set if the user is a host for a lobby.
     */
    public void setIsHost(boolean isHost) {
        this.isHost = isHost;
    }
}

package ee.taltech.voshooter.networking.messages;

public class User {

    private String name;
    private boolean host = false;

    /** @return The name of this user. */
    public String getName() {
        return (name == null) ? "Anonymous" : name;
    }

    /**
     * @return If the user is the host for a lobby.
     */
    public boolean isHost() {
        return host;
    }

    /**
     * @param isHost Set if the user is a host for a lobby.
     */
    public void setHost(boolean isHost) {
        this.host = isHost;
    }

     /**
     * Set the name for this user.
     * @param name The name to set this user's name to.
     * @return Whether the set was successful.
     */
    public boolean setName(String name) {
        this.name = name;
        return true;
    }
}

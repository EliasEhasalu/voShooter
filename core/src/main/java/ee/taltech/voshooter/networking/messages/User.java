package ee.taltech.voshooter.networking.messages;

public class User {

    public String name;
    public String currentLobby;
    public boolean host = false;
    public long id;

    /**
     * Set whether this user object is a host or not.
     * @param isHost Whether the user is a host or not.
     */
    public void setHost(boolean isHost) {
        this.host = isHost;
    }

    /** @return Whether the user is a host or not. */
    public boolean isHost() {
        return host;
    }

    /** @return This user's name. */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set this player to.
     */
    public void setName(String name) {
        this.name = name;
    }

    /** @return String repr of this user. */
    public String toString() {
        return name;
    }
}

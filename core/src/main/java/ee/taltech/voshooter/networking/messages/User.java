package ee.taltech.voshooter.networking.messages;

public class User {

    private String name;

    /** @return The name of this user. */
    public String getName() {
        return (name == null) ? "Anonymous" : name;
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

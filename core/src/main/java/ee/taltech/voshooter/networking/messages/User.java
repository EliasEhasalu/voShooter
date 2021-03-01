package ee.taltech.voshooter.networking.messages;

public class User {
    private String name;

    /** @return The name of this user. */
    public String getName() {
        return (name == null) ? "Anonymous" : name;
    }
}

package ee.taltech.voshooter.networking.messages;


public class SetUsername {

    public String desiredName;

    /** Constructor for serialization. */
    public SetUsername() {
    }

    /** @param desiredName The name you wish to set. */
    public SetUsername(String desiredName) {
        this.desiredName = desiredName;
    }
}

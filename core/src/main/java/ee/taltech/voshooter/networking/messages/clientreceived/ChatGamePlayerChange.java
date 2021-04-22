package ee.taltech.voshooter.networking.messages.clientreceived;

public class ChatGamePlayerChange {

    public String message;

    /** Serialization. */
    public ChatGamePlayerChange() {
    }

    public ChatGamePlayerChange(String message) {
        this.message = message;
    }
}

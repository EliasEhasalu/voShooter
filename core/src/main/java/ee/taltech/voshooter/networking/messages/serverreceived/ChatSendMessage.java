package ee.taltech.voshooter.networking.messages.serverreceived;

public class ChatSendMessage {

    public String message;

    /** Default constructor for serialization. */
    public ChatSendMessage() {
    }

    /**
     * Construct message object.
     * @param message The message that was sent.
     */
    public ChatSendMessage(String message) {
        this.message = message;
    }
}

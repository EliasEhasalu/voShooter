package ee.taltech.voshooter.networking.messages.clientreceived;

public class ChatReceiveMessage {

    public long playerId;
    public String message;

    /** Default constructor for serialization. */
    public ChatReceiveMessage() {
    }

    /**
     * Construct the message to be sent to all players in the lobby.
     * @param playerId The ID of the player who sent the message.
     * @param message The message that was sent.
     */
    public ChatReceiveMessage(long playerId, String message) {
        this.playerId = playerId;
        this.message = message;
    }
}

package ee.taltech.voshooter.networking.messages;

public class LobbyCreated {

    public LobbyEntry entry;

    /** */
    public LobbyCreated() {
    }

    /**
     * @param entry An object specifying the state of the lobby as it is being entered.
     */
    public LobbyCreated(LobbyEntry entry) {
        this.entry = entry;
    }
}

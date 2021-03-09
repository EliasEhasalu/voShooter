package ee.taltech.voshooter.networking.messages;

public class LobbyJoined {

    public LobbyEntry entry;

    /** */
    public LobbyJoined() {
    }

    /**
     * @param entry An object specifying the state of the lobby at the time of joining.
     */
    public LobbyJoined(LobbyEntry entry) {
        this.entry = entry;
    }
}

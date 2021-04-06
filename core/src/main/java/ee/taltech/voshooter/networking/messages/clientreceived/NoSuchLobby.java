package ee.taltech.voshooter.networking.messages.clientreceived;

public class NoSuchLobby {

    public String lobbyCode;

    /** */
    public NoSuchLobby() {
    }

    /**
     * @param code of the attempted lobby
     */
    public NoSuchLobby(String code) {
        lobbyCode = code;
    }
}

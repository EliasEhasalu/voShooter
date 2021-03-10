package ee.taltech.voshooter.networking.messages.serverreceived;

public class JoinLobby {

    public String lobbyCode;

    /** */
    public JoinLobby() {
    }

    /**
     * @param lobbyCode The lobby to join.
     */
    public JoinLobby(String lobbyCode) {
        this.lobbyCode = lobbyCode;
    }
}

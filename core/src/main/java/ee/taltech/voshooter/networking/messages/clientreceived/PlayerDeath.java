package ee.taltech.voshooter.networking.messages.clientreceived;

public class PlayerDeath {

    public long playerId;
    public long killerId;

    /** Serialization. */
    public PlayerDeath() {
    }

    /**
     * Send player death message to all clients in lobby when a player is killed.
     * @param playerId The player that died.
     * @param killerId The player that killed the other player.
     */
    public PlayerDeath(long playerId, long killerId) {
        this.playerId = playerId;
        this.killerId = killerId;
    }
}

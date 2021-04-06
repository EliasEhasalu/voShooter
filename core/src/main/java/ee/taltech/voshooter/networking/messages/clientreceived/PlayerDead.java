package ee.taltech.voshooter.networking.messages.clientreceived;

public class PlayerDead {

    public long id;
    public float timeToRespawn;

    /**
     * Serialization.
     */
    public PlayerDead() {
    }

    /**
     * Send dead message with time until respawn.
     * @param id of the player.
     * @param timeToRespawn time left in seconds.
     */
    public PlayerDead(long id, float timeToRespawn) {
        this.id = id;
        this.timeToRespawn = timeToRespawn;
    }
}

package ee.taltech.voshooter.networking.messages.clientreceived;

public class PlayerDead {

    public float timeToRespawn;

    /**
     * Serialization.
     */
    public PlayerDead() {
    }

    /**
     * Send dead message with time until respawn.
     * @param timeToRespawn time left in seconds.
     */
    public PlayerDead(float timeToRespawn) {
        this.timeToRespawn = timeToRespawn;
    }
}

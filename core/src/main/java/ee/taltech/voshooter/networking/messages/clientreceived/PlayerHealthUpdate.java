package ee.taltech.voshooter.networking.messages.clientreceived;

public class PlayerHealthUpdate {

    public long id;
    public int health;

    /**
     * Serialization.
     */
    public PlayerHealthUpdate() {
    }

    /**
     * @param health the health of the player.
     * @param id the ID of the player.
     */
    public PlayerHealthUpdate(int health, long id) {
        this.health = health;
        this.id = id;
    }
}

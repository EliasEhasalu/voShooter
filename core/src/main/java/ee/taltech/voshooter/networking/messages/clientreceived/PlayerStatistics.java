package ee.taltech.voshooter.networking.messages.clientreceived;

public class PlayerStatistics {

    public long id;
    public int deaths;
    public int kills;

    /** Serialization. */
    public PlayerStatistics() {
    }

    /**
     * @param id of the person.
     * @param deaths the person has.
     * @param kills the person has.
     */
    public PlayerStatistics(long id, int deaths, int kills) {
        this.deaths = deaths;
        this.kills = kills;
        this.id = id;
    }
}

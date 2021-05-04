package ee.taltech.voshooter.networking.messages.clientreceived;

public class PlayerStatistics {

    public long id;
    public int deaths;
    public int kills;
    public double timePassed;

    /** Serialization. */
    public PlayerStatistics() {
    }

    /**
     * @param id of the person.
     * @param deaths the person has.
     * @param kills the person has.
     * @param timePassed .
     */
    public PlayerStatistics(long id, int deaths, int kills, double timePassed) {
        this.deaths = deaths;
        this.kills = kills;
        this.id = id;
        this.timePassed = timePassed;
    }
}

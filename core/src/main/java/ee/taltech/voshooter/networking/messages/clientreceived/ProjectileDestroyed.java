package ee.taltech.voshooter.networking.messages.clientreceived;

public class ProjectileDestroyed {

    public int id;

    public ProjectileDestroyed() {
    }

    /**
     * Constructor.
     */
    public ProjectileDestroyed(int id) {
        this.id = id;
    }
}

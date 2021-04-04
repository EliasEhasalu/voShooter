package ee.taltech.voshooter.networking.messages.clientreceived;

public class ProjectileDestroyed {

    public int id;

    public ProjectileDestroyed() {
    }

    /**
     * Constructor.
     * @param id The id of the projectile to destroy.
     */
    public ProjectileDestroyed(int id) {
        this.id = id;
    }
}

package ee.taltech.voshooter.networking.messages.clientreceived;

import java.util.Set;

public class ProjectilePositions {

    public Set<ProjectilePositionUpdate> updates;

    /** Constructor. */
    public ProjectilePositions() {
    }

    /**
     * Construct a message containing a list of projectile position update messages.
     * @param updates List of update messages.
     */
    public ProjectilePositions(Set<ProjectilePositionUpdate> updates) {
        this.updates = updates;
    }
}

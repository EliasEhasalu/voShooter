package ee.taltech.voshooter.networking.messages.clientreceived;

import java.util.List;

public class ProjectilePositions {

    public List<ProjectilePositionUpdate> updates;

    public ProjectilePositions() {
    }

    public ProjectilePositions(List<ProjectilePositionUpdate> updates) {
        this.updates = updates;
    }
}

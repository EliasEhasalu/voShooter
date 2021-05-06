package ee.taltech.voshooter.networking.messages.clientreceived;

import com.badlogic.gdx.math.Vector2;

public class RailgunFired {

    public Vector2 startPos;
    public Vector2 endPos;

    public RailgunFired() {
    }

    public RailgunFired(Vector2 startPos, Vector2 endPos) {
        this.startPos = startPos;
        this.endPos = endPos;
    }
}

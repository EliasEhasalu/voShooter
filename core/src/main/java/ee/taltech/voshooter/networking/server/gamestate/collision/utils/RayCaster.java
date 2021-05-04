package ee.taltech.voshooter.networking.server.gamestate.collision.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import ee.taltech.voshooter.networking.server.gamestate.Game;

import java.util.HashSet;
import java.util.Set;

public class RayCaster {

    private Fixture lastCollisionFixture;
    private Vector2 lastCollisionPosition;
    private Set<Body> exclusions;

    public RayCollision getFirstCollision(Game game, Vector2 initialPos, Vector2 rayDirection, float maxDistance, Set<Body> excluded) {
        reset();
        exclusions.addAll(getDefaultExclusions(game));

        Vector2 endPos = initialPos.cpy().add(rayDirection.cpy().setLength(maxDistance));
        if (Vector2.dst(initialPos.x, initialPos.y, endPos.x, endPos.y) == 0.0f) return null;
        game.getWorld().rayCast(new CallBack(), initialPos, endPos);

        return (lastCollisionFixture == null) ? null : new RayCollision(
                lastCollisionFixture.getBody(),
                lastCollisionPosition
        );
    }

    private void reset() {
        lastCollisionFixture = null;
        lastCollisionPosition = null;
        exclusions = new HashSet<>();
    }

    private Set<Body> getDefaultExclusions(Game game) {
        Set<Body> exclusions = new HashSet<>();
        exclusions.addAll(RayCastExclusions.getDeadPlayers(game));
        exclusions.addAll(RayCastExclusions.getAllProjectiles(game));

        return exclusions;
    }

    private class CallBack implements RayCastCallback {

        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            if (fixture != null && !exclusions.contains(fixture.getBody())) {
                lastCollisionFixture = fixture;
                lastCollisionPosition = point;
                return fraction;
            }
            return 1;
        }
    }
}

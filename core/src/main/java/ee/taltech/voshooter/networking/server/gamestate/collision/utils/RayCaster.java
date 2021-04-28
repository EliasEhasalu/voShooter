package ee.taltech.voshooter.networking.server.gamestate.collision.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

import java.util.HashSet;

public class RayCaster {

    private Fixture lastCollisionFixture;
    private Vector2 lastCollisionPosition;

    public RayCollision getFirstCollision(World world, Vector2 initialPos, Vector2 rayDirection, float maxDistance, HashSet<Body> excluded) {
        lastCollisionFixture = null;

        Vector2 endPos = initialPos.cpy().add(rayDirection.cpy().setLength(maxDistance));
        world.rayCast(new CallBack(), initialPos, endPos);

        return (lastCollisionFixture == null) ? null : new RayCollision(
                lastCollisionFixture.getBody(),
                lastCollisionPosition
        );
    }

    private class CallBack implements RayCastCallback {

        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            lastCollisionFixture = fixture;
            if (lastCollisionFixture != null) lastCollisionPosition = point;

            return fraction;
        }
    }
}

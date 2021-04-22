package ee.taltech.voshooter.networking.server.gamestate.collision.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.HashSet;

public class Raycaster {

    private static final float PLAYER_RADIUS = 0.2f;

    public static Body getFirstCollision(World world, Vector2 initialPos, Vector2 rayDirection, HashSet<Body> excluded) {
        final float epsilon = 0.01f;
        final float hitTolerance = PLAYER_RADIUS;
        final float maxDist = 50f;

        final int n = (int) Math.ceil(maxDist / epsilon);
        Vector2 hop = new Vector2().add(rayDirection.cpy().setLength(epsilon));
        Vector2 currentPos = initialPos.cpy();

        for (int i = 0; i < n; i++) {
            Array<Body> bodies = new Array<>();
            world.getBodies(bodies);

            for (Body body : bodies) {
                if (body.getPosition().dst(currentPos) < PLAYER_RADIUS && !excluded.contains(body)) {
                    System.out.println(body.toString());
                    return body;
                }
            }

            currentPos.add(hop);
        }

        return null;
    }
}

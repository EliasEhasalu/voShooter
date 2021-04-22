package ee.taltech.voshooter.networking.server.gamestate.collision.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.HashSet;

public class Raycaster {

    public static Body getFirstCollision(World world, Vector2 initialPos, Vector2 rayDirection, HashSet<Body> excluded) {
        final float epsilon = 0.05f;
        final float hitTolerance = 0.01f;
        final float maxDist = 2.5f;

        final int n = (int) Math.ceil(maxDist / epsilon);
        final Vector2 hop = initialPos.cpy().setAngleDeg(rayDirection.angleDeg()).setLength(epsilon);

        for (int i = 0; i < n; i++) {
            Array<Body> bodies = new Array<>();
            world.getBodies(bodies);

            for (Body body : bodies) {
                if (body.getPosition().dst(initialPos) < hitTolerance && !excluded.contains(body)) {
                    System.out.println(body.toString());
                    System.out.println(body.getUserData().toString());
                    return body;
                }
            }

            initialPos.add(hop);
        }

        return null;
    }
}

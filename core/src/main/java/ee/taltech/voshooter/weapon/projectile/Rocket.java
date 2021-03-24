package ee.taltech.voshooter.weapon.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import ee.taltech.voshooter.networking.messages.Player;

public class Rocket extends Projectile {

    private static final float rad = 0.1f;
    private static final float vel = 0.01f;
    private static final float explosionRadius = 0.1f;

    public Rocket(Player owner, Vector2 pos, Vector2 dir) {
        super(owner, pos, dir, vel, rad);
        System.out.println("ROCKET");
    }

    @Override
    public void handleCollision(Object o) {
        if (o instanceof Player) {
            // explode();
        }
    }

    private void explode() {
        Array<Body> bodies = owner.getGame().getBodies();
        Vector2 currPos = body.getPosition();

        for (Body b : bodies) {
            System.out.println("3");
            if (Vector2.dst(currPos.x, currPos.y, b.getPosition().x, b.getPosition().y) < explosionRadius) {
                b.applyLinearImpulse(b.getPosition().sub(currPos), b.getPosition(), true);
            }
            System.out.println("4");
        }

        owner.getGame().getWorld().destroyBody(body);
        System.out.println("5");
    }
}

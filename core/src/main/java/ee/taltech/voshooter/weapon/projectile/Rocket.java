package ee.taltech.voshooter.weapon.projectile;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.networking.messages.Player;

public class Rocket extends Projectile {

    private static final float rad = 0.1f;
    private static final float speed = 0f;
    private static final float explosionRadius = 1f;

    public Rocket(Player owner, Vector2 pos, Vector2 dir) {
        super(Type.ROCKET, owner, pos, dir.scl(speed), rad);
        System.out.println("ROCKET");
    }

    @Override
    public void handleCollision(Object o) {
        if (!(o == owner)) {
//             explode();
        }
    }

    private void explode() {
        Vector2 currPos = body.getPosition();

        for (Player p : owner.getGame().getPlayers()) {
            if (Vector2.dst(currPos.x, currPos.y, p.getPos().x, p.getPos().y) < explosionRadius) {
                p.getBody().applyLinearImpulse(p.getPos().sub(currPos), p.getPos(), true);
            }
        }

        owner.getGame().getWorld().destroyBody(body);
    }
}

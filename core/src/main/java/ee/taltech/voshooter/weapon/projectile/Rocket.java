package ee.taltech.voshooter.weapon.projectile;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.networking.messages.Player;

public class Rocket extends Projectile {

    private static final float SPEED = 20f;
    private static final float EXPLOSION_RADIUS = 1f;

    public Rocket(Player owner, Vector2 pos, Vector2 dir) {
        super(Type.ROCKET, owner, pos, dir.setLength(SPEED));
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
            if (Vector2.dst(currPos.x, currPos.y, p.getPos().x, p.getPos().y) < EXPLOSION_RADIUS) {
                p.getBody().applyLinearImpulse(p.getPos().sub(currPos), p.getPos(), true);
            }
        }

        owner.getGame().getWorld().destroyBody(body);
    }
}

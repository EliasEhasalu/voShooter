package ee.taltech.voshooter.weapon.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import ee.taltech.voshooter.networking.messages.Player;

public class Rocket extends Projectile {

    private static final float SPEED = 20f;
    private static final float EXPLOSION_RADIUS = 10f;
    private static final float EXPLOSION_FORCE = 200f;
    private static final float LIFE_TIME = 2f;

    public Rocket(Player owner, Vector2 pos, Vector2 dir) {
        super(Type.ROCKET, owner, pos, dir.setLength(SPEED), LIFE_TIME);
    }

    @Override
    public void handleCollision(Fixture fix) {
        if (!(fix.getBody().getUserData() == owner)) destroy();
    }

    @Override
    protected void uponDestroy() {
        explode();
    }

    private void explode() {
        Vector2 currPos = body.getPosition();

        for (Player p : owner.getGame().getPlayers()) {
            if (Vector2.dst(currPos.x, currPos.y, p.getPos().x, p.getPos().y) < EXPLOSION_RADIUS) {
                p.getBody().applyLinearImpulse(p.getPos().cpy().sub(currPos).scl(EXPLOSION_FORCE), p.getPos(), true);
            }
        }
    }
}

package ee.taltech.voshooter.weapon.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;

public class Grenade extends Projectile {

    public static final float RADIUS = 0.1f;
    private static final float SPEED = 15f;
    private static final float EXPLOSION_RADIUS = 3.2f;
    private static final float EXPLOSION_FORCE = 100f;
    private static final float LIFE_TIME = 2f;
    private static final int EXPLOSION_DAMAGE = 40;
    private static final int DIRECT_HIT_DAMAGE = 20;

    /**
     * Construct the projectile.
     * @param owner    The player who shot the projectile.
     * @param pos      The position of the projectile.
     */
    public Grenade(Player owner, Vector2 pos, Vector2 dir) {
        super(Type.GRENADE, owner, pos, dir.setLength(SPEED), LIFE_TIME);
    }

    @Override
    public Object getDamageSource() {
        return getOwner();
    }

    @Override
    public void handleCollision(Fixture fix) {
        if (
                !(fix.getBody().getUserData() == owner)
                && !(fix.isSensor())
                && (fix.getBody().getUserData() instanceof Player)
        ) {
            ((Player) fix.getBody().getUserData()).takeDamage(DIRECT_HIT_DAMAGE, this);
            destroy();
        }
    }

    @Override
    protected void uponDestroy() {
        explode();
    }

    private void explode() {
        Vector2 currPos = body.getPosition();

        for (Player p : owner.getPlayerManager().getPlayers()) {
            if (Vector2.dst(currPos.x, currPos.y, p.getPos().x, p.getPos().y) < EXPLOSION_RADIUS) {
                p.getBody().applyLinearImpulse(p.getPos().cpy().sub(currPos).scl(EXPLOSION_FORCE), p.getPos(), true);
                p.takeDamage(EXPLOSION_DAMAGE, this);
            }
        }
    }
}

package ee.taltech.voshooter.weapon.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import ee.taltech.voshooter.networking.messages.Player;

public class ShotgunPellet extends Bullet {

    private static final int DAMAGE = 15;
    private static final float BOUNCE_COST = 0.05f;
    private static final float LIFE_TIME = 0.5f;

    public ShotgunPellet(Player owner, Vector2 pos, Vector2 dir) {
        super(Type.PISTOL_BULLET, owner, pos, dir, LIFE_TIME);
    }

    public void handleCollision(Fixture fix) {
        if (
                !(fix.getBody().getUserData() == owner)
                && (!(fix.getBody().getUserData() instanceof Projectile))
        ) {
            reduceLifeTime(BOUNCE_COST);

            if (fix.getBody().getUserData() instanceof Player) {
                Player p = (Player) fix.getBody().getUserData();
                p.takeDamage(DAMAGE);
                updatePlayers(p, owner);
                destroy();
            }
        }
    }

    @Override
    protected void uponDestroy() {
    }
}

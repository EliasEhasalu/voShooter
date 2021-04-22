package ee.taltech.voshooter.weapon.hitscanweapon;

import com.badlogic.gdx.physics.box2d.Body;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.Raycaster;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.weapon.AmmoWeapon;

import java.util.HashSet;

public class Railgun extends HitscanWeapon implements AmmoWeapon {

    private static final float COOL_DOWN = 1.6f;
    private static final int STARTING_AMMO = 10;
    private static final int DAMAGE = 35;

    public Railgun(Player wielder) {
        super(wielder, COOL_DOWN, STARTING_AMMO, Type.RAILGUN);
    }

    @Override
    public int getMaxAmmo() {
        return STARTING_AMMO;
    }

    @Override
    protected void onFire() {
        Body hitObject = Raycaster.getFirstCollision(
                wielder.getWorld(),
                wielder.getPos(),
                wielder.getViewDirection(),
                new HashSet<Body>() {{ add(wielder.getBody()); }}
        );

        if (hitObject != null && hitObject.getUserData() instanceof Player) {
            Player hitPlayer = (Player) hitObject.getUserData();
            hitPlayer.takeDamage(DAMAGE, this);
        }
    }

    @Override
    public Object getDamageSource() {
        return wielder;
    }
}

package ee.taltech.voshooter.weapon.hitscanweapon;

import com.badlogic.gdx.physics.box2d.Body;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.RayCaster;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.server.gamestate.player.status.DamageDealer;
import ee.taltech.voshooter.weapon.Weapon;

import java.util.HashSet;

public abstract class HitscanWeapon extends Weapon implements DamageDealer {

    private final RayCaster rayCaster = new RayCaster();
    private final float maxDist;
    private final int damage;

    public HitscanWeapon(Player wielder, float coolDown, int startingAmmo, float maxDist, int damage, Weapon.Type type) {
        super(wielder, coolDown, startingAmmo, type);
        this.maxDist = maxDist;
        this.damage = damage;
    }

    protected void onFire() {
        Body hitObject = rayCaster.getFirstCollision(
                wielder.getWorld(),
                wielder.getPos(),
                wielder.getViewDirection(),
                maxDist,
                new HashSet<Body>() {{ add(wielder.getBody()); }}
        );

        if (hitObject != null && hitObject.getUserData() instanceof Player) {
            Player hitPlayer = (Player) hitObject.getUserData();
            hitPlayer.takeDamage(damage, this);
        }
    }
}

package ee.taltech.voshooter.weapon.hitscanweapon;

import com.badlogic.gdx.physics.box2d.Body;
import ee.taltech.voshooter.networking.messages.clientreceived.RailgunFired;
import ee.taltech.voshooter.networking.server.VoConnection;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.PixelToSimulation;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.RayCaster;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.RayCollision;
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
        RayCollision collision = rayCaster.getFirstCollision(
                wielder.getGame(),
                wielder.getPos(),
                wielder.getViewDirection(),
                maxDist,
                new HashSet<>()
        );

        if (collision != null && collision.getCollidedBody() != null) {
            sendLaserDataToClients(collision);

            Body collidedBody = collision.getCollidedBody();
            if (collidedBody != null && collidedBody.getUserData() instanceof Player) {
                Player hitPlayer = (Player) collidedBody.getUserData();
                hitPlayer.takeDamage(damage, this);
            }
        }
    }

    private void sendLaserDataToClients(RayCollision collision) {
        for (VoConnection c : wielder.getGame().getConnections()) {
            c.sendTCP(new RailgunFired(
                    PixelToSimulation.toPixels(wielder.getPos()),
                    PixelToSimulation.toPixels(collision.getCollisionPosition())
            ));
        }
    }
}

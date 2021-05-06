package ee.taltech.voshooter.networking.server.gamestate.player.status;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.weapon.Weapon;

public class Burning extends Debuff implements DamageDealer {

    private static final int TIME = 300;
    private static final int FREQUENCY = 30;
    private static final int DAMAGE = 2;

    public Burning(Player target, Object source, Weapon.Type weaponType) {
        super(Type.BURNING, target, source, TIME, FREQUENCY, weaponType);
    }

    @Override
    protected void applyEffect() {
        target.takeDamage(DAMAGE, this, weaponType);
    }

    @Override
    public Object getDamageSource() {
        return source;
    }
}

package ee.taltech.voshooter.networking.server.gamestate.player.status;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;

public class Burning extends Debuff implements DamageDealer {

    private static final int TIME = 300;
    private static final int FREQUENCY = 30;
    private static final int DAMAGE = 2;

    public Burning(Player target, DamageDealer source) {
        super(Type.BURNING, target, source, TIME, FREQUENCY);
    }

    @Override
    protected void applyEffect() {
        target.takeDamage(DAMAGE, this);
    }

    @Override
    public Object getDamageSource() {
        return source;
    }
}

package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.weaponswitching;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.networking.server.gamestate.player.Bot;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.weapon.Weapon;

import java.util.HashMap;
import java.util.Map;

public class DefaultWeaponSwitchingStrategy implements WeaponSwitchingStrategy {

    private static final Map<Weapon.Type, Float> effectiveDistances = new HashMap<Weapon.Type, Float>(){{
        put(Weapon.Type.SHOTGUN, 2.5f);
        put(Weapon.Type.FLAMETHROWER, 5f);
        put(Weapon.Type.GRENADE_LAUNCHER, 8f);
        put(Weapon.Type.MACHINE_GUN, 12f);
        put(Weapon.Type.ROCKET_LAUNCHER, 17f);
        put(Weapon.Type.RAILGUN, 200f);
    }};
    private Bot bot;

    @Override
    public Weapon.Type getWeaponToSwitchTo(Player closestEnemy) {
        if (closestEnemy == null) return null;
        float distance = Vector2.dst(
                bot.getPos().x, bot.getPos().y,
                closestEnemy.getPos().x, closestEnemy.getPos().y
        );
        System.out.println(distance);
        return getBestWeaponForDistance(distance);
    }

    private Weapon.Type getBestWeaponForDistance(float distance) {
        for (Weapon.Type type : Weapon.Type.values()) {
            if (
                type != Weapon.Type.PISTOL
                && getEffectiveDistance(type) > distance
                && bot.getInventory().getWeaponOfType(type).getRemainingAmmo() > 0
            ) return type;
        }
        return Weapon.Type.PISTOL;
    }

    private float getEffectiveDistance(Weapon.Type type) {
        return effectiveDistances.get(type);
    }

    @Override
    public void setBot(Bot bot) {
        this.bot = bot;
    }
}

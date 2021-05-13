package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.weaponswitching;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.networking.server.gamestate.player.Bot;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.weapon.Weapon;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultWeaponSwitchingStrategy implements WeaponSwitchingStrategy {

    private static final LinkedHashMap<Float, Weapon.Type> EFFECTIVE_DISTANCES = new LinkedHashMap<Float, Weapon.Type>(){{
        put(2.5f, Weapon.Type.SHOTGUN);
        put(5f, Weapon.Type.FLAMETHROWER);
        put(8f, Weapon.Type.GRENADE_LAUNCHER);
        put(12f, Weapon.Type.MACHINE_GUN);
        put(18f, Weapon.Type.ROCKET_LAUNCHER);
        put(26f, Weapon.Type.RAILGUN);
        put(200f, Weapon.Type.ROCKET_LAUNCHER);
    }};
    private Bot bot;

    @Override
    public Weapon.Type getWeaponToSwitchTo(Player closestEnemy) {
        if (closestEnemy == null) return null;
        float distance = Vector2.dst(
                bot.getPos().x, bot.getPos().y,
                closestEnemy.getPos().x, closestEnemy.getPos().y
        );
        return getBestWeaponForDistance(distance);
    }

    private Weapon.Type getBestWeaponForDistance(float distance) {
        for (Map.Entry<Float, Weapon.Type> e : EFFECTIVE_DISTANCES.entrySet()) {
            if (
                e.getKey() >= distance
                && bot.getInventory().getWeaponOfType(e.getValue()).getRemainingAmmo() > 0
            ) return e.getValue();
        }
        return Weapon.Type.PISTOL;
    }

    @Override
    public void setBot(Bot bot) {
        this.bot = bot;
    }
}

package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy.weaponswitching;

import ee.taltech.voshooter.networking.server.gamestate.player.Bot;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.weapon.Weapon;

public interface WeaponSwitchingStrategy {

    Weapon.Type getWeaponToSwitchTo(Player closestEnemy);
    void setBot(Bot bot);
}

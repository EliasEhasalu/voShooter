package ee.taltech.voshooter.weapon;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.weapon.hitscanweapon.Railgun;
import ee.taltech.voshooter.weapon.projectileweapon.Flamethrower;
import ee.taltech.voshooter.weapon.projectileweapon.GrenadeLauncher;
import ee.taltech.voshooter.weapon.projectileweapon.MachineGun;
import ee.taltech.voshooter.weapon.projectileweapon.Pistol;
import ee.taltech.voshooter.weapon.projectileweapon.RocketLauncher;
import ee.taltech.voshooter.weapon.projectileweapon.Shotgun;

public class WeaponFactory {

    public static Weapon getWeaponOfType(Weapon.Type weaponType, Player owner) {
        switch (weaponType) {
            case PISTOL:
                return new Pistol(owner);
            case SHOTGUN:
                return new Shotgun(owner);
            case MACHINE_GUN:
                return new MachineGun(owner);
            case FLAMETHROWER:
                return new Flamethrower(owner);
            case ROCKET_LAUNCHER:
                return new RocketLauncher(owner);
            case GRENADE_LAUNCHER:
                return new GrenadeLauncher(owner);
            case RAILGUN:
                return new Railgun(owner);
            default:
                // No-op
        }
        throw new RuntimeException("No such weapon type defined in WeaponFactory.");
    }
}

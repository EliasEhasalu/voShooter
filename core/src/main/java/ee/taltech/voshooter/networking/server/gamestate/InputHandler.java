package ee.taltech.voshooter.networking.server.gamestate;

import ee.taltech.voshooter.controller.ActionType;
import ee.taltech.voshooter.networking.messages.clientreceived.WeaponChanged;
import ee.taltech.voshooter.networking.messages.serverreceived.ChangeWeapon;
import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.messages.serverreceived.MovePlayer;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerAction;
import ee.taltech.voshooter.networking.messages.serverreceived.Shoot;
import ee.taltech.voshooter.networking.server.VoConnection;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.weapon.Weapon;
import ee.taltech.voshooter.weapon.projectileweapon.Flamethrower;
import ee.taltech.voshooter.weapon.projectileweapon.MachineGun;
import ee.taltech.voshooter.weapon.projectileweapon.Pistol;
import ee.taltech.voshooter.weapon.projectileweapon.RocketLauncher;
import ee.taltech.voshooter.weapon.projectileweapon.Shotgun;

public class InputHandler {

    protected void handleInput(VoConnection connection, PlayerAction action) {
        Player player = connection.getPlayer();

        if (player.isAlive()) {
            if (action instanceof MovePlayer) {
                player.addMoveDirection(((MovePlayer) action).xDir, ((MovePlayer) action).yDir);
            } else if (action instanceof Shoot) {
               player.shoot();
            } else if (action instanceof MouseCoords) {
               player.setViewDirection((MouseCoords) action);
            } else if (action instanceof ChangeWeapon) {
                handleChangeWeapon(connection, (ChangeWeapon) action);
            }
        }
    }

    /**
     * Change the weapon of a player.
     * @param c the connection who's weapon should be changed.
     * @param a the weapon to change to.
     */
    private void handleChangeWeapon(VoConnection c, ChangeWeapon a) {
        Weapon weapon = null;
        if (a.weapon == ActionType.WEAPON_PISTOL) {
            if (!(c.getPlayer().getWeapon() instanceof Pistol)) {
                weapon = new Pistol(c.getPlayer());
            }
        } else if (a.weapon == ActionType.WEAPON_SHOTGUN) {
            if (!(c.getPlayer().getWeapon() instanceof Shotgun)) {
                weapon = new Shotgun(c.getPlayer());
            }
        } else if (a.weapon == ActionType.WEAPON_RPG) {
            if (!(c.getPlayer().getWeapon() instanceof RocketLauncher)) {
                weapon = new RocketLauncher(c.getPlayer());
            }
        } else if (a.weapon == ActionType.WEAPON_FLAMETHROWER) {
            if (!(c.getPlayer().getWeapon() instanceof Flamethrower)) {
                weapon = new Flamethrower(c.getPlayer());
            }
        } else if (a.weapon == ActionType.WEAPON_MACHINE_GUN) {
            if (!(c.getPlayer().getWeapon() instanceof MachineGun)) {
                weapon = new MachineGun(c.getPlayer());
            }
        }
        if (weapon != null) {
            c.getPlayer().setWeapon(weapon);
            c.sendTCP(new WeaponChanged(weapon.getType()));
        }
    }
}

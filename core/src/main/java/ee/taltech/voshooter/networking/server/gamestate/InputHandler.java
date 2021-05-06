package ee.taltech.voshooter.networking.server.gamestate;

import ee.taltech.voshooter.networking.messages.serverreceived.ChangeWeapon;
import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.messages.serverreceived.MovePlayer;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerAction;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerDash;
import ee.taltech.voshooter.networking.messages.serverreceived.Shoot;
import ee.taltech.voshooter.networking.server.VoConnection;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.weapon.Weapon;

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
            } else if (action instanceof PlayerDash) {
                handleDash(connection);
            }
        }
    }

    /**
     * Change the weapon of a player.
     * @param c the connection who's weapon should be changed.
     * @param a the weapon to change to.
     */
    private void handleChangeWeapon(VoConnection c, ChangeWeapon a) {
        Weapon.Type weaponType = null;
        switch (a.weapon) {
            case WEAPON_PISTOL:
                weaponType = Weapon.Type.PISTOL;
                break;
            case WEAPON_SHOTGUN:
                weaponType = Weapon.Type.SHOTGUN;
                break;
            case WEAPON_RPG:
                weaponType = Weapon.Type.ROCKET_LAUNCHER;
                break;
            case WEAPON_FLAMETHROWER:
                weaponType = Weapon.Type.FLAMETHROWER;
                break;
            case WEAPON_MACHINE_GUN:
                weaponType = Weapon.Type.MACHINE_GUN;
                break;
            case WEAPON_GRENADE_LAUNCHER:
                weaponType = Weapon.Type.GRENADE_LAUNCHER;
                break;
            case WEAPON_RAILGUN:
                weaponType = Weapon.Type.RAILGUN;
                break;
            default:
                // No-op.
        }
        if (weaponType != null) c.getPlayer().setWeapon(weaponType);
    }

    private void handleDash(VoConnection connection) {
        connection.getPlayer().dash();
    }
}

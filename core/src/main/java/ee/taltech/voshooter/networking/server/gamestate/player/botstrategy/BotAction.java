package ee.taltech.voshooter.networking.server.gamestate.player.botstrategy;

import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.weapon.Weapon;

public class BotAction {

    private boolean dashing;
    private boolean shooting;
    private MouseCoords aim;
    private int[] movementDirections;
    private Weapon.Type weaponToSwitchTo;

    public boolean isDashing() {
        return dashing;
    }

    public boolean isShooting() {
        return shooting;
    }

    public MouseCoords getAim() {
        return aim;
    }

    public int getXMoveDir() {
        return movementDirections[0];
    }

    public int getYMoveDir() {
        return movementDirections[1];
    }

    public Weapon.Type getWeaponToSwitchTo() {
        return weaponToSwitchTo;
    }

    void setDashing(boolean dashing) {
        this.dashing = dashing;
    }

    void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    void setAim(MouseCoords aim) {
        this.aim = aim;
    }

    void setMovementDirections(int[] movementDirections) {
        this.movementDirections = movementDirections;
    }

    void setWeaponToSwitchTo(Weapon.Type weaponToSwitchTo) {
        this.weaponToSwitchTo = weaponToSwitchTo;
    }
}

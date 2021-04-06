package ee.taltech.voshooter.networking.messages.serverreceived;

import ee.taltech.voshooter.controller.ActionType;

public class ChangeWeapon extends PlayerAction {

    public ActionType weapon;

    /** Serialization. */
    public ChangeWeapon() {
    }

    /**
     * @param weapon to change to.
     */
    public ChangeWeapon(ActionType weapon) {
        this.weapon = weapon;
    }
}

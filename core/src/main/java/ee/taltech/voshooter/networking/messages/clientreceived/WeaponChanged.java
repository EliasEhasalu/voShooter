package ee.taltech.voshooter.networking.messages.clientreceived;

import ee.taltech.voshooter.controller.ActionType;

public class WeaponChanged {

    public ActionType weapon;

    public WeaponChanged() {
    }

    /**
     * @param weapon The weapon to change to on the client.
     */
    public WeaponChanged(ActionType weapon) {
        this.weapon = weapon;
    }
}

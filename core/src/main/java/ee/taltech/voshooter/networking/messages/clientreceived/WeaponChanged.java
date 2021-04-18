package ee.taltech.voshooter.networking.messages.clientreceived;

import ee.taltech.voshooter.weapon.Weapon;

public class WeaponChanged {

    public Weapon.Type weapon;

    public WeaponChanged() {
    }

    /**
     * @param weapon The weapon to change to on the client.
     */
    public WeaponChanged(Weapon.Type weapon) {
        this.weapon = weapon;
    }
}

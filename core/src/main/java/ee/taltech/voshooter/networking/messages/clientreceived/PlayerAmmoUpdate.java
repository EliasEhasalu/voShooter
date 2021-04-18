package ee.taltech.voshooter.networking.messages.clientreceived;

import ee.taltech.voshooter.weapon.Weapon;

public class PlayerAmmoUpdate {

    public Weapon.Type weaponType;
    public int remainingAmmo;

    public PlayerAmmoUpdate() {
    }

    public PlayerAmmoUpdate(Weapon.Type weaponType, int remainingAmmo) {
        this.weaponType = weaponType;
        this.remainingAmmo = remainingAmmo;
    }

}

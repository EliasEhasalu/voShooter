package ee.taltech.voshooter.networking.messages.clientreceived;

import ee.taltech.voshooter.weapon.Weapon;

public class PlayerSwappedWeapon {

    public long id;
    public Weapon.Type weaponType;

    public PlayerSwappedWeapon() {
    }

    public  PlayerSwappedWeapon(long id, Weapon.Type weaponType) {
        this.id = id;
        this.weaponType = weaponType;
    }
}

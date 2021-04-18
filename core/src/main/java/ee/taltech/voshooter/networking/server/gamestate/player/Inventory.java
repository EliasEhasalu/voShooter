package ee.taltech.voshooter.networking.server.gamestate.player;

import ee.taltech.voshooter.weapon.Weapon;
import ee.taltech.voshooter.weapon.WeaponBuilder;

import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private static final Weapon.Type DEFAULT_WEAPON = Weapon.Type.PISTOL;
    private static final int FREQ = 2400;

    private final Map<Weapon.Type, Weapon> weapons = new HashMap<>();
    private final Player parent;
    private Weapon currentWeapon;
    private int ticks = 0;

    protected Inventory(Player parent) {
        this.parent = parent;
        pickUpWeapon(Weapon.Type.PISTOL);
        pickUpWeapon(Weapon.Type.MACHINE_GUN);
        pickUpWeapon(Weapon.Type.FLAMETHROWER);
        pickUpWeapon(Weapon.Type.ROCKET_LAUNCHER);
        pickUpWeapon(Weapon.Type.SHOTGUN);
        swapToDefaultWeapon();
    }

    protected void attemptToFireCurrentWeapon() {
        getCurrentWeapon().fire();
        if (getCurrentWeapon().getRemainingAmmo() == 0) swapToDefaultWeapon();
    }

    protected void update() {
        getCurrentWeapon().coolDown();
        replenishAmmo();

        modulo();
    }

    private void replenishAmmo() {
        final int frequency = 600;
        if ((ticks % frequency) == 0) {
            for (Weapon weapon : weapons.values()) {
                if (weapon != null) weapon.replenishAmmoBy(0.1f);
            }
        }
    }

    private void modulo() {
        ticks++;
        ticks %= FREQ;
    }

    private boolean canSwapToWeaponOfType(Weapon.Type weaponType) {
        return (
            weapons.containsKey(weaponType)
            && weapons.get(weaponType).getRemainingAmmo() > 0
        );
    }

    public void swapToWeapon(Weapon.Type weaponType) {
        if (canSwapToWeaponOfType(weaponType)) currentWeapon = weapons.get(weaponType);
    }

    public void swapToDefaultWeapon() {
        swapToWeapon(DEFAULT_WEAPON);
    }

    protected Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void pickUpWeapon(Weapon.Type weaponType) {
        if (weapons.containsKey(weaponType)) weapons.get(weaponType).replenishAmmo();
        else weapons.put(weaponType, WeaponBuilder.getWeaponOfType(weaponType, parent));
    }
}

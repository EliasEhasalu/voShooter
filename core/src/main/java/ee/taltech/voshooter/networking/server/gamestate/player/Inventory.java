package ee.taltech.voshooter.networking.server.gamestate.player;

import ee.taltech.voshooter.networking.messages.clientreceived.PlayerSwappedWeapon;
import ee.taltech.voshooter.weapon.Weapon;
import ee.taltech.voshooter.weapon.WeaponFactory;

import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private static final Weapon.Type DEFAULT_WEAPON = Weapon.Type.PISTOL;
    private static final int FREQ = 2400;
    private static final float PASSIVE_AMMO_REGENERATION_FACTOR = 0.1f;

    private final Map<Weapon.Type, Weapon> weapons = new HashMap<>();
    private final Player parent;
    private Weapon currentWeapon;
    private Weapon swappedWeapon;
    private int ticks = 0;

    protected Inventory(Player parent) {
        this.parent = parent;
        pickUpWeapon(Weapon.Type.PISTOL);
        pickUpWeapon(Weapon.Type.MACHINE_GUN);
        pickUpWeapon(Weapon.Type.FLAMETHROWER);
        pickUpWeapon(Weapon.Type.ROCKET_LAUNCHER);
        pickUpWeapon(Weapon.Type.SHOTGUN);
        pickUpWeapon(Weapon.Type.GRENADE_LAUNCHER);
        pickUpWeapon(Weapon.Type.RAILGUN);
        swapToWeapon(Weapon.Type.RAILGUN);
    }

    protected void attemptToFireCurrentWeapon() {
        getCurrentWeapon().fire();
        if (getCurrentWeapon().getRemainingAmmo() == 0) swapToDefaultWeapon();
    }

    protected void update() {
        getCurrentWeapon().coolDown();
        passiveAmmoRegeneration();

        modulo();
    }

    public void sendUpdates() {
        if (swappedWeapon != null) parent.getGame().getConnections().forEach(
                c -> c.sendTCP(new PlayerSwappedWeapon(parent.getId(), swappedWeapon.getType()))
        );

        clearTemporaryData();
    }

    private void clearTemporaryData() {
        swappedWeapon = null;
    }

    private void passiveAmmoRegeneration() {
        final int frequency = 600;
        if ((ticks % frequency) == 0) {
            replenishWeaponAmmoBy(PASSIVE_AMMO_REGENERATION_FACTOR);
        }
    }

    public void replenishWeaponAmmoBy(float replenishmentFactor) {
        for (Weapon weapon : weapons.values()) {
            if (weapon != null) weapon.replenishAmmoBy(replenishmentFactor);
        }
    }

    private void modulo() {
        ticks++;
        ticks %= FREQ;
    }

    private boolean canSwapToWeaponOfType(Weapon.Type weaponType) {
        return (
            (currentWeapon == null || currentWeapon.getType() != weaponType)
            && weapons.containsKey(weaponType)
            && weapons.get(weaponType).getRemainingAmmo() > 0
        );
    }

    public void swapToWeapon(Weapon.Type weaponType) {
        if (canSwapToWeaponOfType(weaponType)) {
            currentWeapon = weapons.get(weaponType);
            swappedWeapon = currentWeapon;
        }
    }

    public void swapToDefaultWeapon() {
        swapToWeapon(DEFAULT_WEAPON);
    }

    protected Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void pickUpWeapon(Weapon.Type weaponType) {
        if (weapons.containsKey(weaponType)) weapons.get(weaponType).replenishAmmo();
        else weapons.put(weaponType, WeaponFactory.getWeaponOfType(weaponType, parent));
    }
}

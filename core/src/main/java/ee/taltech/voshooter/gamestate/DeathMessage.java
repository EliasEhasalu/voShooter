package ee.taltech.voshooter.gamestate;

import ee.taltech.voshooter.entity.player.ClientPlayer;
import ee.taltech.voshooter.weapon.Weapon;

public class DeathMessage {

    private final ClientPlayer player;
    private final ClientPlayer killer;
    private final Weapon.Type weaponType;
    private float duration = 420;

    public DeathMessage(ClientPlayer player, ClientPlayer killer, Weapon.Type weaponType) {
        this.player = player;
        this.killer = killer;
        this.weaponType = weaponType;
    }

    public boolean tick() {
        duration -= 1;
        return duration <= 0;
    }

    public ClientPlayer getPlayer() {
        return player;
    }

    public ClientPlayer getKiller() {
        return killer;
    }

    public Weapon.Type getWeaponType() {
        return weaponType;
    }

    public float getDuration() {
        return duration;
    }
}

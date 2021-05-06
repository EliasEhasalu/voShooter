package ee.taltech.voshooter.networking.messages.clientreceived;

import ee.taltech.voshooter.weapon.Weapon;

public class PlayerDeath {

    public long playerId;
    public long killerId;
    public Weapon.Type weaponType;

    /** Serialization. */
    public PlayerDeath() {
    }

    /**
     * Send player death message to all clients in lobby when a player is killed.
     * @param playerId The player that died.
     * @param killerId The player that killed the other player.
     */
    public PlayerDeath(long playerId, long killerId, Weapon.Type weaponType) {
        this.playerId = playerId;
        this.killerId = killerId;
        this.weaponType = weaponType;
    }

    public PlayerDeath(long playerId) {
        this.playerId = playerId;
        this.killerId = playerId;
    }
}

package ee.taltech.voshooter.gamestate;

import ee.taltech.voshooter.entity.player.ClientPlayer;

public class DeathMessage {

    private final ClientPlayer player;
    private final ClientPlayer killer;
    private float duration = 420;

    public DeathMessage(ClientPlayer player, ClientPlayer killer) {
        this.player = player;
        this.killer = killer;
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

    public float getDuration() {
        return duration;
    }
}

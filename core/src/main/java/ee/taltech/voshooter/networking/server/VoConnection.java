package ee.taltech.voshooter.networking.server;

import com.esotericsoftware.kryonet.Connection;

import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.networking.messages.User;

public class VoConnection extends Connection {

    public transient User user;
    public transient Player player;

    /**
     * @return The player object associated with this connection.
     */
    public Player getPlayer() {
       return player;
    }
}

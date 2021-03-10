package ee.taltech.voshooter.networking.server;

import com.esotericsoftware.kryonet.Connection;

import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.networking.messages.User;

public class VoConnection extends Connection {

    public User user;
    public Player player;
}

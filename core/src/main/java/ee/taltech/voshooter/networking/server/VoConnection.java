package ee.taltech.voshooter.networking.server;

import com.esotericsoftware.kryonet.Connection;

import ee.taltech.voshooter.networking.User;

public class VoConnection extends Connection {
    User user = new User();
}

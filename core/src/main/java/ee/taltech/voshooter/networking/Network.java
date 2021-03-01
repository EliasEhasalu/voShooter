package ee.taltech.voshooter.networking;

import java.util.ArrayList;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

import ee.taltech.voshooter.networking.messages.Lobby;
import ee.taltech.voshooter.networking.messages.User;

public final class Network {

    public static final int PORT = 54569;

    // These IDs are used to register objects in ObjectSpaces.
    public static final short REMOTE = 1;
    public static final short SERVER_ENTRY = 2;

    /** Hide public constructor. */
    private Network() {
    }

    /**
     * Register all classes that will be passed over the network.
     * @param endPoint The server.
     */
    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        ObjectSpace.registerClasses(kryo);

        // Register all classes transported over the connection.
        kryo.register(String[].class);
        kryo.register(ArrayList.class);

        kryo.register(RemoteInterface.class);
        kryo.register(ClientInterface.class);
        kryo.register(User.class);
        kryo.register(Lobby.class);
    }
}

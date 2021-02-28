package ee.taltech.voshooter.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

public final class Network {

    public static final int PORT = 54569;

    /**
     * Hide public constructor.
     */
    private Network() {
    }

    /**
     * Register all classes that will be passed over the network.
     * @param endPoint The server.
     */
    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        // The interfaces that will be used as remote objects must be registered.
        ObjectSpace.registerClasses(kryo);
        // The classes of all method parameters and return values
        // for remote objects must also be registered.
        kryo.register(String[].class);

        kryo.register(Hello.class);
        kryo.register(CreateLobby.class);
        kryo.register(LobbyCreated.class);
    }

    public static class Hello {
        public String greeting;
    }

    public static class CreateLobby {
    }

    public static class LobbyCreated {
        public String lobbyId;
    }
}

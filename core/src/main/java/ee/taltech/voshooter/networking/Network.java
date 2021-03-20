package ee.taltech.voshooter.networking;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

import ee.taltech.voshooter.controller.PlayerAction;
import ee.taltech.voshooter.geometry.Pos;
import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.networking.messages.User;
import ee.taltech.voshooter.networking.messages.clientreceived.*;
import ee.taltech.voshooter.networking.messages.serverreceived.CreateLobby;
import ee.taltech.voshooter.networking.messages.serverreceived.JoinLobby;
import ee.taltech.voshooter.networking.messages.serverreceived.LeaveLobby;
import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerInput;
import ee.taltech.voshooter.networking.messages.serverreceived.SetUsername;
import ee.taltech.voshooter.networking.messages.serverreceived.StartGame;
import ee.taltech.voshooter.networking.server.gamestate.Draggable;

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
        kryo.register(List.class);
        kryo.register(String.class);
        kryo.register(ArrayList.class);
        kryo.register(boolean.class);

        kryo.register(Player.class);
        kryo.register(Pos.class);
        kryo.register(PlayerInput.class);
        kryo.register(PlayerAction.class);
        kryo.register(MouseCoords.class);
        kryo.register(Vector2.class);
        kryo.register(PlayerPositionUpdate.class);
        kryo.register(PlayerViewUpdate.class);

        kryo.register(User.class);
        kryo.register(SetUsername.class);
        kryo.register(CreateLobby.class);
        kryo.register(LobbyJoined.class);
        kryo.register(JoinLobby.class);
        kryo.register(LeaveLobby.class);
        kryo.register(LobbyFull.class);
        kryo.register(NoSuchLobby.class);
        kryo.register(LobbyUserUpdate.class);
        kryo.register(StartGame.class);
        kryo.register(GameStarted.class);
        kryo.register(Draggable.class);
    }
}

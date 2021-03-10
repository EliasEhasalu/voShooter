package ee.taltech.voshooter.networking.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * Code snippet courtesy of VaTTeRGeR
 * On thread https://jvm-gaming.org/t/kryonet-best-way-to-send-read-simple-tcp-messages/56626
 * Accessed 2021-03-10.
 *
 * @param <T> The class of message to listen to.
 */
public abstract class RunMethodListener<T> extends Listener {

    private final Class<T> clazz;

    /**
     * Construct the listener.
     * @param clazz The class of message to listen to.
     */
    public RunMethodListener(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * @param c The connection where the message was received.
     * @param o The message object received.
     */
    @Override
    public void received(Connection c, Object o) {
        if (clazz.isInstance(o)) {
            VoConnection con = (VoConnection) c;
            run(con, clazz.cast(o));
        }
    }

    /**
     * Implement how to handle receiving the message.
     * @param c The connection the message was received on.
     * @param received The received message.
     */
    public abstract void run(VoConnection c, T received);
}

package ee.taltech.voshooter.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import ee.taltech.voshooter.networking.server.VoServer;
import ee.taltech.voshooter.networking.server.VoServer.Remote;
import ee.taltech.voshooter.networking.messages.Lobby;

public class VoServerTest {

    private static final int LOBBY_CODE_LENGTH = 6;
    private VoServer s;
    private Remote r;

    //CHECKSTYLE:OFF
    @AfterEach
    void cleanUp() {
        s.close();
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        try {
            s = new VoServer();
            r = s.new Remote();
        } catch (IOException e) {
        }
    }

    @Test
    void shouldConstruct() {
        try {
            s = new VoServer();
        } catch (IOException e) {
        }
    }

    @Test
    void shouldCreateLobby() {
        Lobby l = r.createLobby();
        assertTrue(l instanceof Lobby);
    }

    @Test
    void createdLobbyShouldHaveCode() {
        Lobby l = r.createLobby();

        String code = l.getLobbyCode();
        assertEquals(LOBBY_CODE_LENGTH, code.length());
    }

    @Test
    void createdLobbyCodeShouldBeUnique() {
        Map<String, Integer> codes = new HashMap<String, Integer>();

        for (int i = 0; i < 10; i++) {
            Lobby l = r.createLobby();
            String code = l.getLobbyCode();
            codes.put(code, codes.getOrDefault(code, 0) + 1);
            assertTrue(codes.get(code) <= 1);
        }
    }
}

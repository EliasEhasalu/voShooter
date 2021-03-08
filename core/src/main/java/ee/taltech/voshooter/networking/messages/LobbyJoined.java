package ee.taltech.voshooter.networking.messages;

import java.util.ArrayList;
import java.util.List;

public class LobbyJoined {

    public boolean wasSuccessful;
    public List<User> users = new ArrayList<>();
}

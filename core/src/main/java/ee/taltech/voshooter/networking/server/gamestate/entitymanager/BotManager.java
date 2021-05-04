package ee.taltech.voshooter.networking.server.gamestate.entitymanager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class BotManager {

    private static final Set<String> BOT_NAMES = new HashSet<String>() {{
        add("Josh"); add("Bill"); add("Jake"); add("Gaben");
        add("Alice"); add("Lisa"); add("Lucy"); add("Ago");
    }};
    private static final Set<String> currentlyUsedBotNames = new HashSet<>();
    private static final Random r = new Random();
    private static long ID = Long.MAX_VALUE;

    long getNewBotId() {
        return ID--;
    }

    String getNewBotName() {
        String candidate;

        while (true) {
            String name = "";
            int randInt = r.nextInt(BOT_NAMES.size()) + 1;
            Iterator<String> nameIterator = BOT_NAMES.iterator();
            for (int i = 0; i < randInt; i++) name = nameIterator.next();
            candidate = "BOT " + name;

            if (!currentlyUsedBotNames.contains(candidate)) {
                currentlyUsedBotNames.add(candidate);
                return candidate;
            }
        }
    }
}

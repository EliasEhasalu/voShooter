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
    private static final Random R = new Random();
    private static long id = Long.MAX_VALUE;

    private final Set<String> currentlyUsedBotNames = new HashSet<>();

    long getNewBotId() {
        return id--;
    }

    String getNewBotName() {
        String candidate;

        int j = 0;
        while (true) {
            String name = "";
            int randInt = R.nextInt(BOT_NAMES.size()) + 1;
            Iterator<String> nameIterator = BOT_NAMES.iterator();
            for (int i = 0; i < randInt; i++) name = nameIterator.next();
            candidate = "BOT " + name;

            if (!currentlyUsedBotNames.contains(candidate)) {
                currentlyUsedBotNames.add(candidate);
                return candidate;
            }
            if (j > 1000000) return "BOT Lucky";
            j++;
        }
    }
}

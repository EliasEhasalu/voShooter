package ee.taltech.voshooter.networking.server.gamestate.entitymanager;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerSpawner {

    private static final List<Vector2> SPAWN_POINTS = new ArrayList<Vector2>() {{
        add(new Vector2(6, 6));
        add(new Vector2(6, 58));
        add(new Vector2(58, 6));
        add(new Vector2(58, 58));
        add(new Vector2(32, 32));
    }};
    private static final Random R = new Random();

    /**
     * @return A spawn point for a player.
     */
    public static Vector2 getSpawnPoint() {
        return SPAWN_POINTS.get(R.nextInt(SPAWN_POINTS.size()));
    }
}

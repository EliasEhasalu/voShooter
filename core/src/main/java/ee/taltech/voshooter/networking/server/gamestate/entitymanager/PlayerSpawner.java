package ee.taltech.voshooter.networking.server.gamestate.entitymanager;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlayerSpawner {

    private int currentMap = 1;
    private final Random R = new Random();

    private final List<Vector2> map1Spawns = new ArrayList<Vector2>() {{
        add(new Vector2(6, 6));
        add(new Vector2(6, 58));
        add(new Vector2(58, 6));
        add(new Vector2(58, 58));
        add(new Vector2(32, 32));
    }};

    private final Map<Integer, List<Vector2>> SPAWN_POINTS = new HashMap<Integer, List<Vector2>>() {{
        put(1, map1Spawns);
    }};


    /**
     * @return A spawn point for a player.
     */
    public Vector2 getSpawnPointForCurrentMap() {
        return SPAWN_POINTS.get(currentMap).get(R.nextInt(SPAWN_POINTS.size()));
    }

    public void setMap(int map) {
        if (map <= 0) throw new IllegalArgumentException();

        currentMap = map;
    }
}

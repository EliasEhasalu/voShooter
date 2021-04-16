package ee.taltech.voshooter.networking.server.gamestate.entitymanager;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.map.GameMap;

import java.util.List;
import java.util.Random;

public class PlayerSpawner {

    private final Random R = new Random();

    /**
     * @param mapType The map for which the spawn point will be returned.
     * @return A spawn point for a player.
     */
    public Vector2 getSpawnPointForCurrentMap(GameMap.MapType mapType) {
        List<Vector2> spawnPoints = GameMap.getSpawnLocations(mapType);
        return spawnPoints.get(R.nextInt(spawnPoints.size()));
    }
}

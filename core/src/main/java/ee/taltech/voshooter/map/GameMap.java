package ee.taltech.voshooter.map;

import com.badlogic.gdx.math.Vector2;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameMap {

    public enum MapType {
        DEFAULT,
        TEST1
    }

    // Tileset used by map.
    private static final Map<MapType, String> TILESET_MAP = Stream.of(
            new AbstractMap.SimpleEntry<>(MapType.DEFAULT, "tileset/voShooterMap.tmx"),
            new AbstractMap.SimpleEntry<>(MapType.TEST1, "tileset/voShooterMap.tmx"))

            .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));


    // Spawn locations for map.
    private static final Map<MapType, List<Vector2>> SPAWN_MAP = Stream.of(
            new AbstractMap.SimpleEntry<>(MapType.DEFAULT, Arrays.asList(
                    new Vector2(10, 10),
                    new Vector2(10, 100))),
            new AbstractMap.SimpleEntry<>(MapType.TEST1, Collections.singletonList(
                    new Vector2(10, 10))))

            .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

    /**
     * @param mapType The map type to return.
     * @return Tile set of the map type.
     */
    public static String getTileSet(MapType mapType) {
        return TILESET_MAP.getOrDefault(mapType, TILESET_MAP.get(MapType.DEFAULT));
    }

    /**
     * @param mapType The desired map.
     * @return List of spawn locations.
     */
    public static List<Vector2> getSpawnLocations(MapType mapType) {
        return SPAWN_MAP.getOrDefault(mapType, SPAWN_MAP.get(MapType.DEFAULT));
    }
}

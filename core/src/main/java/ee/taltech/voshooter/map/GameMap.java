package ee.taltech.voshooter.map;

import com.badlogic.gdx.math.Vector2;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameMap {

    public enum MapType {
        DEFAULT,
        MAP2,
        FUNKY,
        WILD_RIDE
    }

    // Maps available in game.
    public static final MapType[] PLAYER_MAPS = {
            MapType.DEFAULT,
            MapType.MAP2,
            MapType.FUNKY,
            MapType.WILD_RIDE
    };


    // Tileset used by map.
    private static final Map<MapType, String> TILESET_MAP = Stream.of(
            new AbstractMap.SimpleEntry<>(MapType.DEFAULT, "tileset/vo_shooter_map.tmx"),
            new AbstractMap.SimpleEntry<>(MapType.MAP2, "tileset/map2.tmx"),
            new AbstractMap.SimpleEntry<>(MapType.FUNKY, "tileset/funky_map.tmx"),
            new AbstractMap.SimpleEntry<>(MapType.WILD_RIDE, "tileset/wild_ride.tmx")
    )
            .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));


    // Spawn locations for map.
    private static final Map<MapType, List<Vector2>> SPAWN_MAP = Stream.of(
            new AbstractMap.SimpleEntry<>(MapType.DEFAULT, Arrays.asList(
                    new Vector2(6, 6),
                    new Vector2(6, 58),
                    new Vector2(58, 6),
                    new Vector2(58, 58),
                    new Vector2(32, 32))),
            new AbstractMap.SimpleEntry<>(MapType.MAP2, Arrays.asList(
                    new Vector2(58, 58),
                    new Vector2(10, 10))),
            new AbstractMap.SimpleEntry<>(MapType.FUNKY, Arrays.asList(
            new Vector2(3, 3),
                    new Vector2(3, 61),
                    new Vector2(61, 3),
                    new Vector2(61, 16))))

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

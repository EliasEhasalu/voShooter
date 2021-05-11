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
        GRAND,
        FUNKY,
        MAP2,
        WILD_RIDE
        }

    // Maps available in game.
    public static final MapType[] PLAYER_MAPS = {
            MapType.DEFAULT,
            MapType.GRAND,
            MapType.FUNKY,
            //MapType.MAP2,
            //MapType.WILD_RIDE
    };

    // Tileset used by map.
    private static final Map<MapType, String> TILESET_MAP = Stream.of(
            new AbstractMap.SimpleEntry<>(MapType.DEFAULT, "tileset/vo_shooter_map.tmx"),
            new AbstractMap.SimpleEntry<>(MapType.GRAND, "tileset/grand.tmx"),
            new AbstractMap.SimpleEntry<>(MapType.FUNKY, "tileset/funky_map.tmx"),
            new AbstractMap.SimpleEntry<>(MapType.MAP2, "tileset/map2.tmx"),
            new AbstractMap.SimpleEntry<>(MapType.WILD_RIDE, "tileset/wild_ride.tmx")
    )
            .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));


    // Spawn locations for map.
    private static final Map<MapType, List<Vector2>> SPAWN_MAP = Stream.of(
            new AbstractMap.SimpleEntry<>(MapType.DEFAULT, Arrays.asList(
                    new Vector2(10, 3),
                    new Vector2(3, 10),
                    new Vector2(55, 61),
                    new Vector2(61, 55),
                    new Vector2(61, 9),
                    new Vector2(55, 3),
                    new Vector2(3, 55),
                    new Vector2(9, 61),
                    new Vector2(3, 29),
                    new Vector2(3, 35),
                    new Vector2(29, 3),
                    new Vector2(35, 3),
                    new Vector2(61, 29),
                    new Vector2(61, 35),
                    new Vector2(29, 61),
                    new Vector2(35, 61),
                    new Vector2(25, 25),
                    new Vector2(25, 38),
                    new Vector2(38, 25),
                    new Vector2(38, 38))),
            new AbstractMap.SimpleEntry<>(MapType.GRAND, Arrays.asList(
                    new Vector2(8, 91 - 8),
                    new Vector2(22, 91 - 9),
                    new Vector2(9, 91 - 25),
                    new Vector2(66, 91 - 15),
                    new Vector2(85, 91 - 6),
                    new Vector2(86, 91 - 18),
                    new Vector2(8, 91 - 70),
                    new Vector2(10, 91 - 82),
                    new Vector2(25, 91 - 82),
                    new Vector2(71, 91 - 82),
                    new Vector2(77, 91 - 74),
                    new Vector2(82, 91 - 65)
                    )),
            new AbstractMap.SimpleEntry<>(MapType.FUNKY, Arrays.asList(
                    new Vector2(61, 32),
                    new Vector2(45, 47),
                    new Vector2(32, 60),
                    new Vector2(19, 47),
                    new Vector2(3, 32),
                    new Vector2(19, 16),
                    new Vector2(32, 2),
                    new Vector2(45, 16),
                    new Vector2(3, 3),
                    new Vector2(3, 61),
                    new Vector2(61, 3),
                    new Vector2(61, 61))),
            new AbstractMap.SimpleEntry<>(MapType.MAP2, Arrays.asList(
                    new Vector2(12, 40),
                    new Vector2(11, 7),
                    new Vector2(8, 52),
                    new Vector2(8, 59),
                    new Vector2(35, 46),
                    new Vector2(30, 46),
                    new Vector2(12, 42),
                    new Vector2(35, 41),
                    new Vector2(44, 4),
                    new Vector2(4, 4))),
            new AbstractMap.SimpleEntry<>(MapType.WILD_RIDE, Arrays.asList(
                    new Vector2(3, 41),
                    new Vector2(3, 3),
                    new Vector2(41, 3),
                    new Vector2(41, 41),
                    new Vector2(18, 18),
                    new Vector2(3, 74),
                    new Vector2(27, 74),
                    new Vector2(38, 82),
                    new Vector2(28, 96),
                    new Vector2(28, 100),
                    new Vector2(7, 102),
                    new Vector2(2, 100),
                    new Vector2(24, 78)
            )))

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

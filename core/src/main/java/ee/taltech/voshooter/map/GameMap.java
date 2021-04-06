package ee.taltech.voshooter.map;

import com.badlogic.gdx.math.Vector2;

import java.util.AbstractMap;
import java.util.ArrayList;
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

    private static final Map<MapType, String> TILESET_MAP = Stream.of(
            new AbstractMap.SimpleEntry<>(MapType.DEFAULT, "tileset/voShooterMap.tmx"),
            new AbstractMap.SimpleEntry<>(MapType.TEST1, "tileset/voShooterMap.tmx"))
            .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

    private static final Map<MapType, List<Vector2>> SPAWN_MAP = Stream.of(
            new AbstractMap.SimpleEntry<>(MapType.DEFAULT, Collections.singletonList(
                    new Vector2(10, 10))),
            new AbstractMap.SimpleEntry<>(MapType.TEST1, ))
            .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

    /**
     * @param mapType The map type to return.
     * @return Tile set of the map type.
     */
    public static String getTileSet(MapType mapType) {
        return TILESET_MAP.getOrDefault(mapType, TILESET_MAP.get(MapType.DEFAULT));
    }
}

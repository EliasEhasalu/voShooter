package ee.taltech.voshooter.networking.server.gamestate.collision.utils;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.LinkedHashMap;
import java.util.Map;

public class LevelGenerator {

    private static final int NODE_SIZE_IN_PIXELS = 32;

    public static void generateLevel(World world, TiledMap map) {
        addWallsToWorld(world, map);
    }

    private static void addWallsToWorld(World world, TiledMap map) {
        MapObjects objects;

        for (MapLayer l : map.getLayers()) {
            if (l.getName().equals("WallsObjects")) {
                objects = l.getObjects();
                for (MapObject object : objects) {
                    Shape shape;

                    if (object instanceof RectangleMapObject) {
                        shape = ShapeFactory.getRectangle((RectangleMapObject) object);
                    } else if (object instanceof PolygonMapObject) {
                        shape = ShapeFactory.getPolygon((PolygonMapObject) object);
                    } else if (object instanceof PolylineMapObject) {
                        shape = ShapeFactory.getPolyline((PolylineMapObject) object);
                    } else if (object instanceof CircleMapObject) {
                        shape = ShapeFactory.getCircle((CircleMapObject) object);
                    } else {
                        continue;
                    }

                    Vector2 pos = getMapObjectWorldPos(object);

                    BodyDef bodyDef = new BodyDef();
                    bodyDef.type = BodyDef.BodyType.StaticBody;
                    bodyDef.position.x = pos.x;
                    bodyDef.position.y = pos.y;
                    Body body = world.createBody(bodyDef);

                    FixtureDef fixtureDef = new FixtureDef();
                    fixtureDef.shape = shape;
                    fixtureDef.density = 0f;
                    fixtureDef.restitution = 0f;

                    Fixture fixture = body.createFixture(fixtureDef);

                    shape.dispose();
                }
                break;
            }
        }
    }

    private static Vector2 getMapObjectWorldPos(MapObject object) {
        if (object instanceof RectangleMapObject) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            return new Vector2(
                    PixelToSimulation.toUnits(rectangle.x + rectangle.width * 0.5f),
                    PixelToSimulation.toUnits(rectangle.y + rectangle.height * 0.5f)
            );
        }
        throw new RuntimeException("World position not defined for this shape.");
    }

    public static Map<Integer, Float> getKothLocation(TiledMap map) {
        for (MapLayer l : map.getLayers()) {
            if (l.getName().equals("koth")) {
                MapObject object = l.getObjects().get(0);
                Map<Integer, Float> corners = new LinkedHashMap<>();
                if (object instanceof RectangleMapObject) {
                    corners.put(0, PixelToSimulation.toUnits(((RectangleMapObject) object).getRectangle().x));
                    corners.put(1, PixelToSimulation.toUnits((((RectangleMapObject) object).getRectangle().x
                            + ((RectangleMapObject) object).getRectangle().width)));
                    corners.put(2, PixelToSimulation.toUnits(((RectangleMapObject) object).getRectangle().y));
                    corners.put(3, PixelToSimulation.toUnits((((RectangleMapObject) object).getRectangle().y
                            + ((RectangleMapObject) object).getRectangle().height)));
                }
                return corners;
            }
        }
        return null;
    }

    private static int[] getMapObjectGridPos(MapObject object) {
        if (object instanceof RectangleMapObject) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            return new int[] {
                    (int) PixelToSimulation.toUnits(rectangle.x),
                    (int) PixelToSimulation.toUnits(rectangle.y)
            };
        }
        throw new RuntimeException("Grid position not defined for this shape.");
    }

    private static int[] getMapObjectGridDimensions(MapObject object) {
        if (object instanceof RectangleMapObject) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            return new int[]{
                    (int) PixelToSimulation.toUnits(rectangle.getWidth()),
                    (int) PixelToSimulation.toUnits(rectangle.getHeight())
            };
        }
        throw new RuntimeException("Grid dimensions not defined for this shape.");
    }

    public static int[][] getWallGrid(TiledMap map) {
        int width = (int) map.getProperties().get("width");
        int height = (int) map.getProperties().get("height");

        MapObjects objects;
        int[][] grid = new int[width][height];

        for (MapLayer l : map.getLayers()) {
            if (l.getName().equals("WallsObjects")) {
                objects = l.getObjects();
                for (MapObject object : objects) {
                    int[] pos = getMapObjectGridPos(object);
                    int[] dimensions = getMapObjectGridDimensions(object);
                    int x = pos[0];
                    int y = pos[1];
                    int w = dimensions[0];
                    int h = dimensions[1];
                    for (int i = 0; i < h; i++) {
                        for (int j = 0; j < w; j++) {
                            grid[y + i][x + j] = 1;
                        }
                    }
                }
                break;
            }
        }

        return grid;
    }
}

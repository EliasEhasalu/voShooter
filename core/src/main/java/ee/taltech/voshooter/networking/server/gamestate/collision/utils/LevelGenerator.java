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

                    Vector2 pos = getMapObjectPos(object);

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
                    System.out.println(body.getPosition());

                    shape.dispose();
                }
                break;
            }
        }
    }

    private static Vector2 getMapObjectPos(MapObject object) {
        if (object instanceof RectangleMapObject) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            return new Vector2(
                    PixelToSimulation.toUnits(rectangle.x + rectangle.width * 0.5f),
                    PixelToSimulation.toUnits(rectangle.y + rectangle.height * 0.5f)
            );
        }
        throw new RuntimeException("Position not defined for this shape.");
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
}

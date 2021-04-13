package ee.taltech.voshooter.networking.server.gamestate.collision.utils;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

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

                    BodyDef bodyDef = new BodyDef();
                    bodyDef.type = BodyDef.BodyType.StaticBody;
                    Body body = world.createBody(bodyDef);
                    Fixture f = body.createFixture(shape, 1);
                    f.setFriction(0f);
                    f.setRestitution(1f);

                    shape.dispose();
                }
                break;
            }
        }
    }
}

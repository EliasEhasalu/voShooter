package ee.taltech.voshooter.networking.server.gamestate.collision.utils;

import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import ee.taltech.voshooter.weapon.projectile.Fireball;
import ee.taltech.voshooter.weapon.projectile.PistolBullet;
import ee.taltech.voshooter.weapon.projectile.Projectile;
import ee.taltech.voshooter.weapon.projectile.Rocket;

import java.util.Arrays;
import java.util.List;

public final class ShapeFactory {

    public static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        return getRectangle(rectangleObject.getRectangle());
    }

    public static PolygonShape getRectangle(Rectangle rectangle) {
        PolygonShape polygon = new PolygonShape();

        Vector2 size = new Vector2(
                PixelToSimulation.toUnits(rectangle.x + rectangle.width * 0.5f),
                PixelToSimulation.toUnits(rectangle.y + rectangle.height * 0.5f)
        );

        polygon.setAsBox(
                PixelToSimulation.toUnits(rectangle.width * 0.5f),
                PixelToSimulation.toUnits(rectangle.height * 0.5f),
                size,
                0.0f
        );

        return polygon;
    }

    public static CircleShape getCircle(CircleMapObject circleObject) {
        return getCircle(circleObject.getCircle());
    }

    public static CircleShape getCircle(Circle circle) {
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(PixelToSimulation.toUnits(circle.radius));
        circleShape.setPosition(new Vector2(PixelToSimulation.toUnits(circle.x), PixelToSimulation.toUnits(circle.y)));

        return circleShape;
    }

    public static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        return getPolygon(polygonObject.getPolygon());
    }

    public static PolygonShape getPolygon(Polygon polygon) {
        PolygonShape polygonShape = new PolygonShape();

        float[] vertices = polygon.getTransformedVertices();
        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = PixelToSimulation.toUnits(vertices[i]);
        }

        polygonShape.set(worldVertices);

        return polygonShape;
    }

    public static ChainShape getPolyline(PolylineMapObject polylineObject) {
        return getPolyline(polylineObject.getPolyline());
    }

    public static ChainShape getPolyline(Polyline polyline) {
        ChainShape chain = new ChainShape();

        float[] vertices = polyline.getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = PixelToSimulation.toUnits(vertices[i * 2]);
            worldVertices[i].y = PixelToSimulation.toUnits(vertices[i * 2 + 1]);
        }

        chain.createChain(worldVertices);

        return chain;
    }

    public static CircleShape getCircle(Vector2 pos, float radius) {
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        circleShape.setPosition(new Vector2(pos.x, pos.y));

        return circleShape;
    }

    public static Body getProjectileBody(Projectile.Type type, World world, Vector2 pos, Vector2 vel) {
        switch (type) {
            case ROCKET:
                return getRocket(world, pos, vel);
            case PISTOL_BULLET:
                return getPistolBullet(world, pos, vel);
            case FIREBALL:
                return getFireball(world, pos, vel);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Body getFireball(World world, Vector2 pos, Vector2 vel) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(pos);

        Body b = world.createBody(def);
        CircleShape shape = new CircleShape();
        shape.setRadius(Fireball.RADIUS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 10f;

        Fixture fixture = b.createFixture(fixtureDef);
        fixture.setSensor(true);  // Turn off collisions.

        b.setLinearVelocity(vel);
        shape.dispose();
        return b;
    }

    public static Body getRocket(World world, Vector2 pos, Vector2 vel) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(pos);

        Body b = world.createBody(def);
        CircleShape shape = new CircleShape();
        shape.setRadius(Rocket.RADIUS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 10f;

        Fixture fixture = b.createFixture(fixtureDef);

        b.setLinearVelocity(vel);
        shape.dispose();
        return b;
    }

    public static Body getPistolBullet(World world, Vector2 pos, Vector2 vel) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(pos);

        Body b = world.createBody(def);
        CircleShape shape = new CircleShape();
        shape.setRadius(PistolBullet.RADIUS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 5f;

        Fixture fixture = b.createFixture(fixtureDef);

        b.applyLinearImpulse(vel.cpy().nor(), b.getPosition(), true);
        shape.dispose();
        return b;
    }

    public static List<Object> getPlayerFixtureAndBody(World world, Vector2 pos) {
        // Create a body definition.
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(pos);

        // Create the body and set its bounding box.
        // Add the body to the world.
        Body body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.4f, 0.4f);

        // Set its physical properties.
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 10f;

        Fixture fixture = body.createFixture(fixtureDef);
        body.setLinearDamping(6f);

        shape.dispose();
        return Arrays.asList(fixture, body);
    }
}

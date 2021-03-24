package ee.taltech.voshooter.weapon.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.networking.server.gamestate.collision.ShapeFactory;

public abstract class Projectile {

    protected Body body;
    protected Vector2 vel;
    protected Player owner;


    public Projectile(Player owner, Vector2 pos, Vector2 dir, float speed, float rad) {
        Shape shape = ShapeFactory.getCircle(pos, rad);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = owner.getGame().getWorld().createBody(bodyDef);
        body.createFixture(shape, 1);

        shape.dispose();

        body.setUserData(this);  // Have the body remember this rocket object.
        vel = dir.limit(speed);
        body.setLinearVelocity(vel);
    }

    public abstract void handleCollision(Object o);
}

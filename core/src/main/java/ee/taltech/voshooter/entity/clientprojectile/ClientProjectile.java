package ee.taltech.voshooter.entity.clientprojectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.entity.Entity;
import ee.taltech.voshooter.networking.messages.clientreceived.ProjectileCreated;
import ee.taltech.voshooter.rendering.Drawable;
import ee.taltech.voshooter.weapon.projectile.Projectile;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClientProjectile extends Entity implements Drawable {

    private static final Map<Projectile.Type, String> SPRITE_MAP = Stream.of(
            new AbstractMap.SimpleEntry<>(Projectile.Type.ROCKET, "textures/projectiles/rocketProjectile.png"))
            .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

    private final int id;
    private final Projectile.Type type;
    private final Sprite sprite;
    private final float spriteScale = -0.5f;

    /**
     * Constructor.
     * @param msg Update message.
     */
    public ClientProjectile(ProjectileCreated msg) {
        super(msg.pos, msg.vel);
        this.id = msg.id;
        this.type = msg.type;
        this.sprite = new Sprite(new Texture(SPRITE_MAP.getOrDefault(type, "textures/projectiles/rocketProjectile.png")));
        this.sprite.scale(spriteScale);
        this.sprite.setCenterX(getPosition().x);
        this.sprite.setCenterY(getPosition().y);
        System.out.println(position);
    }

    /**
     * Set the player's position.
     * @param pos The position to set the player to.
     */
    public void setPos(Vector2 pos) {
        this.position = pos;
        this.sprite.setCenterX(pos.x);
        this.sprite.setCenterY(pos.y);
    }

    public void setVel(Vector2 vel) {
        this.velocity = vel;
        this.sprite.setRotation(vel.angleDeg());
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public float getScale() {
        return spriteScale;
    }

    public int getId() {
        return id;
    }
}

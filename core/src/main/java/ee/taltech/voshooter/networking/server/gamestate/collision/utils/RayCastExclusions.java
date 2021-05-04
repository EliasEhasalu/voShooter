package ee.taltech.voshooter.networking.server.gamestate.collision.utils;

import com.badlogic.gdx.physics.box2d.Body;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;
import ee.taltech.voshooter.weapon.projectile.Projectile;

import java.util.Set;
import java.util.stream.Collectors;

public class RayCastExclusions {

    public static Set<Body> getDeadPlayers(Game game) {
       return game.getPlayers().stream()
               .filter(p -> !p.isAlive())
               .map(Player::getBody)
               .collect(Collectors.toSet());
    }

    public static Set<Body> getAllProjectiles(Game game) {
        return game.getEntityManagerHub().getAllProjectiles().stream()
                .map(Projectile::getBody)
                .collect(Collectors.toSet());
    }
}

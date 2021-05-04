package ee.taltech.voshooter.networking.server.gamestate.entitymanager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerAmmoUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerDead;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerHealthUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerPositionUpdate;
import ee.taltech.voshooter.networking.messages.clientreceived.PlayerViewUpdate;
import ee.taltech.voshooter.networking.server.VoConnection;
import ee.taltech.voshooter.networking.server.gamestate.Game;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.PixelToSimulation;
import ee.taltech.voshooter.networking.server.gamestate.collision.utils.ShapeFactory;
import ee.taltech.voshooter.networking.server.gamestate.player.Bot;
import ee.taltech.voshooter.networking.server.gamestate.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager extends EntityManager {

    private final Set<Player> players = ConcurrentHashMap.newKeySet();
    private final PlayerSpawner playerSpawner = new PlayerSpawner();
    private final BotManager botManager = new BotManager();

    public PlayerManager(World world, Game game) {
        super(world, game);
    }

    protected void createPlayer(VoConnection c) {
        addPlayerToWorld(new Player(this, c, c.user.id, c.user.getName()));
    }

    protected void createBot() {
        addPlayerToWorld(new Bot(this, botManager.getNewBotId(), botManager.getNewBotName()));
    }

    private void addPlayerToWorld(Player p) {
        List<Object> fixtureAndBody = ShapeFactory.getPlayerFixtureAndBody(world, playerSpawner.getSpawnPointForMap(game.getMapType()));
        Body playerBody = (Body) fixtureAndBody.get(1);
        Fixture playerFixture = (Fixture) fixtureAndBody.get(0);

        p.setBody(playerBody);
        p.setFixture(playerFixture);
        playerBody.setUserData(p);
        p.initialPos = playerBody.getPosition();

        // Set the player object on the connection.
        if (!p.isBot()) p.getConnection().player = p;

        players.add(p);
    }

    @Override
    protected void update() {
        players.forEach(Player::update);
    }

    @Override
    protected void sendUpdates() {
        for (Player p : players) {
            for (VoConnection c : game.getConnections()) {
                if (p.getBody() != null) {
                    c.sendTCP(new PlayerHealthUpdate(p.getHealth(), p.getId()));
                    c.sendTCP(new PlayerPositionUpdate(PixelToSimulation.toPixels(p.getPos()), p.getId()));
                    c.sendTCP(new PlayerViewUpdate(p.getViewDirection(), p.getId()));
                    if (p.getId() == c.user.id) c.sendTCP(new PlayerAmmoUpdate(p.getWeapon().getType(), p.getWeapon().getRemainingAmmo()));
                    if (!(p.isAlive())) c.sendTCP(new PlayerDead(p.getId(), p.getTimeToRespawn()));
                }
            }
            p.getInventory().sendUpdates();
        }
    }

    @Override
    public void add(Object player) {
        Player p = (Player) player;

        players.add(p);
    }

    public void remove(long id) {
        Optional<Player> player = players.stream().filter(p -> p.getId() == id).findFirst();
        if (player.isPresent()) {
            player.get().purge();
            players.remove(player.get());
        }
    }

    public World getWorld() {
        return world;
    }

    public Game getGame() {
        return game;
    }

    public Vector2 getSpawnPoint() {
        return playerSpawner.getSpawnPointForMap(game.getMapType());
    }

    public Set<Player> getPlayers() {
        return players;
    }
}

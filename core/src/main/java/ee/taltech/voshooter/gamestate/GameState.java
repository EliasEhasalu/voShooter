package ee.taltech.voshooter.gamestate;

import java.util.ArrayList;
import java.util.List;

import ee.taltech.voshooter.networking.messages.serverreceived.PlayerAction;
import ee.taltech.voshooter.entity.Entity;
import ee.taltech.voshooter.entity.player.ClientPlayer;
import ee.taltech.voshooter.networking.messages.Player;
import ee.taltech.voshooter.networking.messages.User;
import ee.taltech.voshooter.rendering.Drawable;

public class GameState {

    private final List<Entity> entities = new ArrayList<>();
    private final List<Drawable> drawableEntities = new ArrayList<>();

    public ClientLobby currentLobby = new ClientLobby(this);

    public User clientUser = new User();
    public List<ClientPlayer> players = new ArrayList<>();
    public ClientPlayer userPlayer;
    public List<PlayerAction> currentInputs = new ArrayList<>();

    /**
     * @return The list of drawable entities.
     */
    public List<Drawable> getDrawables() {
        return drawableEntities;
    }

    /**
     * @return The list of players.
     */
    public List<ClientPlayer> getPlayers() {
        return players;
    }

    /**
     * Act as a single game tick.
     */
    public void tick() {
       //.
    }

    /**
     * Add an entity to be tracked.
     * @param e The entity to be tracked.
     */
    public void addEntity(Entity e) {
        if (!entities.contains(e)) {
            entities.add(e);

            if (e instanceof Drawable) {
                drawableEntities.add((Drawable) e);
            }

            if (e instanceof ClientPlayer) {
                players.add((ClientPlayer) e);
            }
        }
    }

    /**
     * Create all the player objects.
     * @param players List of players.
     */
    public void createPlayerObjects(List<Player> players) {
        for (Player p : players) {
            ClientPlayer newP = new ClientPlayer(p.getPos(), p.getId(), p.getName());
            addEntity(newP);
            if (p.getId() == clientUser.id) {
                userPlayer = newP;
            }
        }
    }
}

package ee.taltech.voshooter.gamestate;

import java.util.ArrayList;
import java.util.List;

import ee.taltech.voshooter.controller.GameController;
import ee.taltech.voshooter.entity.Entity;
import ee.taltech.voshooter.entity.player.Player;
import ee.taltech.voshooter.networking.messages.User;
import ee.taltech.voshooter.rendering.Drawable;

public class GameState {

    private List<Entity> entities = new ArrayList<>();
    private List<Drawable> drawableEntities = new ArrayList<>();
    public ClientLobby currentLobby = new ClientLobby(this);
    public User clientUser = new User();
    public GameController controller;
    public Player playerCharacter;

    /**
     * @return The list of drawable entities.
     */
    public List<Drawable> getDrawables() {
        return drawableEntities;
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
     * @return Whether the entity was added successfully.
     */
    public boolean addEntity(Entity e) {
        if (!entities.contains(e)) {
            entities.add(e);

            if (e instanceof Drawable) {
                drawableEntities.add((Drawable) e);
            }
            return true;
        }
        return false;
    }

    /**
     * Create a game controller if one does not already exist.
     * @return If the controller was created.
     */
    public boolean createController() {
        if (controller == null) {
            controller = new GameController();
            return true;
        }
        return false;
    }
}

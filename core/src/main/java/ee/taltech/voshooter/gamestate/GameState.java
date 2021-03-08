package ee.taltech.voshooter.gamestate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ee.taltech.voshooter.entity.Entity;
import ee.taltech.voshooter.networking.messages.User;
import ee.taltech.voshooter.rendering.Drawable;

public class GameState {

    private List<Entity> entities = new ArrayList<>();
    public ClientLobby currentLobby = new ClientLobby();
    public User clientUser = new User();

    /**
     * @return The list of drawable entities.
     */
    public List<Drawable> getDrawables() {
        return entities.stream()
            .filter(e -> e instanceof Drawable)
            .map(e -> (Drawable) e)
            .collect(Collectors.toList());
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
            return true;
        }
        return false;
    }
}

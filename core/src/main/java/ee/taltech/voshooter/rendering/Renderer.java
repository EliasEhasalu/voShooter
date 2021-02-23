package ee.taltech.voshooter.rendering;

import com.badlogic.gdx.graphics.g2d.Batch;

import ee.taltech.voshooter.gamestate.GameState;

public class Renderer {

    private GameState gameState;
    private Batch batch;

    /**
     * Construct this renderer with the given drawables.
     * @param batch The batch to draw onto.
     * @param gameState The gamestate to poll drawables from.
     */
    public Renderer(Batch batch, GameState gameState) {
        this.batch = batch;
        this.gameState = gameState;
    }

    /**
     * Draw all objects allocated to this renderer.
     */
    public void draw() {
        for (Drawable d : gameState.getDrawables()) d.getSprite().draw(batch);
    }

    /**
     * Deallocate texture memory.
     */
    public void clean() {
        for (Drawable d : gameState.getDrawables()) d.getSprite().getTexture().dispose();
    }
}

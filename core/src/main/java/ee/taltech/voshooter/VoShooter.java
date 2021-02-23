package ee.taltech.voshooter;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;

import ee.taltech.voshooter.gamestate.GameState;
import ee.taltech.voshooter.rendering.Renderer;

public final class VoShooter extends Game {

    private SpriteBatch batch;
    private Renderer renderer;
    private GameState gameState;

    /**
     * Used to initialize assets.
     */
    @Override
    public void create() {
        // Initialize sprite layer.
        gameState = new GameState();
        batch = new SpriteBatch();
        renderer = new Renderer(batch, gameState.getDrawables());
    }

    /**
     * Used as a rendering loop.
     */
    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        gameState.tick();
        renderer.draw();

        batch.end();
    }

    /**
     * Used to free memory of assets.
     */
    @Override
    public void dispose() {
        batch.dispose();
    }
}

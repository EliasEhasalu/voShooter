package ee.taltech.voshooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public final class Game extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture texture;
    private Sprite sprite;

    /**
     * Used to initialize assets.
     */
    @Override
    public void create() {
        // Initialize sprite layer.
        batch = new SpriteBatch();

        // Initialize textures.
        texture = new Texture("badlogic.jpg");

        // Initialize sprites.
        final int initialPos = 200;

        sprite = new Sprite(texture);
        sprite.setPosition(initialPos, initialPos);
    }

    /**
     * Used as a rendering loop.
     */
    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // Draw sprite onto batch.
        sprite.draw(batch);

        batch.end();
    }

    /**
     * Used to free memory of assets.
     */
    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
    }
}

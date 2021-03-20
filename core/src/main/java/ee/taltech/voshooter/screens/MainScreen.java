package ee.taltech.voshooter.screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.voshooter.VoShooter;
import ee.taltech.voshooter.controller.GameController;
import ee.taltech.voshooter.controller.PlayerAction;
import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerInput;
import ee.taltech.voshooter.rendering.Drawable;


public class    MainScreen implements Screen {

    private final VoShooter parent;
    private final Stage stage;
    public VoShooter.Screen shouldChangeScreen;
    OrthographicCamera camera;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    /**
     * Construct the menu screen.
     * @param parent A reference to the orchestrator object.
     */
    public MainScreen(VoShooter parent) {
        this.parent = parent;

        // Create stage which will contain this screen's objects
        stage = new Stage(new ScreenViewport());
    }

    /**
     * Define the assets to be shown in the render loop.
     */
    @Override
    public void show() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        camera.update();
        tiledMap = new TmxMapLoader().load("tileset/voShooterMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        // Have it handle player's input.
        Gdx.input.setInputProcessor(stage);
        stage.clear();
    }

    /**
     * Render the elements defined in the show() method.
     */
    @Override
    public void render(float delta) {
        // Send player inputs to server every render loop.
        List<PlayerAction> inputs = GameController.getInputs();
        if (!inputs.isEmpty()) parent.getClient().sendTCP(new PlayerInput(inputs));
        // Get the mouse coordinates in world space.
        Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        parent.getClient().sendTCP(new MouseCoords(mousePos.x, mousePos.y));

        // Refresh the graphics renderer every cycle.
        Gdx.gl.glClearColor(0.25882354f, 0.25882354f, 0.90588236f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleInputs();
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // And draw over it again.
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 64f));  // Cap menu FPS to 64.
        stage.draw();

        // Draw drawable entities to the stage.
        stage.getBatch().setProjectionMatrix(camera.combined);
        stage.getBatch().begin();
        for (Drawable drawable : parent.gameState.getDrawables()) {
            drawable.getSprite().draw(stage.getBatch());
        }
        stage.getBatch().end();
    }

    /**
     * Move camera when needed.
     */
    private void handleInputs() {
        for (PlayerAction input : GameController.getInputs()) {
            if (input == PlayerAction.MOVE_LEFT)
                camera.translate(0, 0);
            if (input == PlayerAction.MOVE_RIGHT)
                camera.translate(0, 0);
            if (input == PlayerAction.MOVE_UP)
                camera.translate(0, 0);
            if (input == PlayerAction.MOVE_DOWN)
                camera.translate(0, 0);
        }
    }

    /**
     * Make sure the window doesn't break.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Placeholder.
     */
    @Override
    public void pause() {
    }

    /**
     * Placeholder.
     */
    @Override
    public void resume() {
    }

    /**
     * Placeholder.
     */
    @Override
    public void hide() {
    }

    /**
     * Dispose of the screen.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}

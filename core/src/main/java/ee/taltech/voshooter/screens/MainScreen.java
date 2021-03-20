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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.voshooter.VoShooter;
import ee.taltech.voshooter.controller.GameController;
import ee.taltech.voshooter.controller.PlayerAction;
import ee.taltech.voshooter.geometry.Pos;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerInput;
import ee.taltech.voshooter.rendering.Drawable;


public class MainScreen implements Screen {

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

        // Refresh the graphics renderer every cycle.
        Gdx.gl.glClearColor(0.25882354f, 0.25882354f, 0.90588236f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        moveCameraToPlayer();
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
     * Set the camera position to the players position.
     */
    private void moveCameraToPlayer() {
        final float maxCameraDist = 150;
        final float minCameraTranslate = 0.3f;
        final Pos playerPos = parent.gameState.userPlayer.getPosition();
        final Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        Vector2 vecToPanPoint = new Vector2(mousePos.x - playerPos.getX(), mousePos.y - playerPos.getY());
        vecToPanPoint.limit(maxCameraDist);
        final Vector2 vecFromCamera = new Vector2(playerPos.getX() + vecToPanPoint.x - camera.position.x,
                playerPos.getY() + vecToPanPoint.y - camera.position.y);

        float xTranslate = vecFromCamera.x / 15;
        float yTranslate = vecFromCamera.y / 15;

        camera.translate(xTranslate, yTranslate);

        // camera.position.set(playerPos.getX(), playerPos.getY(), camera.position.z);
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

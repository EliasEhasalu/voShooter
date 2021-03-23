package ee.taltech.voshooter.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.voshooter.VoShooter;
import ee.taltech.voshooter.controller.ActionType;
import ee.taltech.voshooter.controller.GameController;
import ee.taltech.voshooter.entity.player.ClientPlayer;
import ee.taltech.voshooter.geometry.Pos;
import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.messages.serverreceived.MovePlayer;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerAction;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerInput;
import ee.taltech.voshooter.networking.messages.serverreceived.Shoot;
import ee.taltech.voshooter.rendering.Drawable;
import ee.taltech.voshooter.soundeffects.MusicPlayer;


public class MainScreen implements Screen {

    private final VoShooter parent;
    private final Stage stage;
    public VoShooter.Screen shouldChangeScreen;
    private BitmapFont font;
    private final Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
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
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(1.5f);
        MusicPlayer.stopMusic();

        // Have it handle player's input.
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        createMenuButtons();
    }

    /**
     * Render the elements defined in the show() method.
     */
    @Override
    public void render(float delta) {
        // Send player inputs to server every render loop.
        handlePlayerInputs();

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
            if (drawable instanceof ClientPlayer) {
                font.draw(stage.getBatch(), ((ClientPlayer) drawable).getName(),
                        drawable.getPosition().getX() - (((ClientPlayer) drawable).getName().length() * 6),
                        drawable.getPosition().getY() + 40);
            }
        }
        stage.getBatch().end();
    }

    /**
     * Send player inputs to server.
     * Might also perform other things based on inputs client-side in the future.
     */
    private void handlePlayerInputs() {
        List<ActionType> inputs = GameController.getInputs();
        Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        List<PlayerAction> inputsToSend = new ArrayList<>();
        inputsToSend.add(new MouseCoords(mousePos.x, mousePos.y));

        for (ActionType a : inputs) {
            switch (a) {
                case MOVE_LEFT:
                    inputsToSend.add(new MovePlayer(-1, 0));
                    break;
                case MOVE_RIGHT:
                    inputsToSend.add(new MovePlayer(1, 0));
                    break;
                case MOVE_UP:
                    inputsToSend.add(new MovePlayer(0, 1));
                    break;
                case MOVE_DOWN:
                    inputsToSend.add(new MovePlayer(0, -1));
                    break;
                case MOUSE_LEFT:
                    inputsToSend.add(new Shoot());
                    break;
                default:
                    break;
            }

        }

        // Send all inputs this frame to server.
        parent.getClient().sendTCP(new PlayerInput(inputsToSend));
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
     * Create the buttons for the menu and add functionality to them.
     */
    private void createMenuButtons() {
        // A Skin object defines the theme for menu objects.
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        // Add a table which will contain menu items to the stage.
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Create the objects in the scene.
        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton exitButton = new TextButton("Exit", skin);
        resumeButton.setVisible(false);
        settingsButton.setVisible(false);
        exitButton.setVisible(false);

        // Add the buttons to the table.
        table.add(resumeButton).fillX();
        table.row().padTop(10);
        table.add(settingsButton).fillX();
        table.row().padTop(10);
        table.add(exitButton).fillX();

        // Add button functionality.
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    if (resumeButton.isVisible()) {
                        resumeButton.setVisible(false);
                        settingsButton.setVisible(false);
                        exitButton.setVisible(false);
                    } else {
                        resumeButton.setVisible(true);
                        settingsButton.setVisible(true);
                        exitButton.setVisible(true);
                    }
                }
                return true;
            }
        });

        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                resumeButton.setVisible(false);
                settingsButton.setVisible(false);
                exitButton.setVisible(false);
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                resumeButton.setVisible(false);
                settingsButton.setVisible(false);
                exitButton.setVisible(false);
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(VoShooter.Screen.MENU);
            }
        });
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

package ee.taltech.voshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.voshooter.VoShooter;

import static ee.taltech.voshooter.VoShooter.Screen.CREATE_GAME;
import static ee.taltech.voshooter.VoShooter.Screen.MENU;

public class CreateGameScreen implements Screen {

    private VoShooter parent;
    private Stage stage;
    private Label playerCountIndicator;
    private Label playerGameModeIndicator;

    /**
     * Construct the menu screen.
     * @param parent A reference to the orchestrator object.
     */
    public CreateGameScreen(VoShooter parent) {
        this.parent = parent;

        // Create stage which will contain this screen's objects
        stage = new Stage(new ScreenViewport());
    }

    /**
     * Define the assets to be shown in the render loop.
     */
    @Override
    public void show() {
        // Have it handle player's input.
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        // Add a table which will contain game creation settings.
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // A Skin object defines the theme for menu objects.
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        // Create the menu objects for our stage.
        Label playerCount = new Label("Players: ", skin);
        Label gameMode = new Label("Gamemode: ", skin);
        TextButton back = new TextButton("Back", skin);
        TextButton createGame = new TextButton("Create game", skin);
        Slider playerCountSlider = new Slider(0, 16, 1, false, skin);
        Slider playerGameModeSlider = new Slider(1, 4, 1, false, skin);
        playerCountIndicator = new Label(String.valueOf(playerCountSlider.getValue()), skin);
        playerGameModeIndicator = new Label(String.valueOf(playerGameModeSlider.getValue()), skin);

        // Add the buttons to the table.
        table.pad(100, 0, 0, 0);
        table.add(playerCount).fillX().uniformX();
        table.add(playerCountSlider).fillX().uniformX();
        table.add(playerCountIndicator).fillX().uniformX().pad(0, 10, 0, 0);
        table.row().pad(10, 0, 10, 0);
        table.add(gameMode).fillX().uniformX();
        table.add(playerGameModeSlider).fillX().uniformX();
        table.add(playerGameModeIndicator).fillX().uniformX().pad(0, 10, 0, 0);
        table.row().pad(10, 0, 10, 0);
        table.add(createGame).fillX().uniformX().colspan(2);
        table.row().pad(200, 0, 0, 0);
        table.add(back).fillX().uniformX().colspan(2);

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                parent.changeScreen(MENU);
            }
        });

        createGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(MENU);
            }
        });

        playerCountSlider.setValue(parent.getPreferences().getSoundVolume());
        playerCountSlider.addListener(event ->  {
            parent.getPreferences().setPlayerCount(playerCountSlider.getValue());
            playerCountIndicator.setText(String.valueOf(playerCountSlider.getValue()));
            return false;
        });

        playerGameModeSlider.setValue(parent.getPreferences().getSoundVolume());
        playerGameModeSlider.addListener(event ->  {
            parent.getPreferences().setGameMode(playerGameModeSlider.getValue());
            playerGameModeIndicator.setText(String.valueOf(playerGameModeSlider.getValue()));
            return false;
        });
    }

    /**
     * Render the elements defined in the show() method.
     */
    @Override
    public void render(float delta) {
        // Refresh the graphics renderer every cycle.
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // And draw over it again.
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));  // Cap menu FPS to 30.
        stage.draw();
    }

    /**
     * Make sure the window doesn't break.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void update() {

    }
}

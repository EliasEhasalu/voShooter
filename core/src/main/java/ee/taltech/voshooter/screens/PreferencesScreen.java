package ee.taltech.voshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.voshooter.AppPreferences;
import ee.taltech.voshooter.VoShooter;
import ee.taltech.voshooter.soundeffects.MusicPlayer;

public class PreferencesScreen implements Screen {

    private VoShooter parent;
    private Stage stage;
    private Label volumeMusicIndicator;
    private Label volumeSoundIndicator;

    /**
     * Construct the preferences screen. Pass in a reference to the orchestrator.
     * @param parent A reference to the orchestrator to enable communication.
     */
    public PreferencesScreen(VoShooter parent) {
        this.parent = parent;

        // Create stage which will contain this screen's objects
        stage = new Stage(new ScreenViewport());
    }

    /**
     * Initialize UI elements to be drawn.
     */
    @Override
    public void show() {
        // Have it handle player's input.
        Gdx.input.setInputProcessor(stage);
        // Add a table which will contain settings items to the stage.
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // A Skin object defines the theme for settings objects.
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        // Create the settings objects for our stage.
        final Slider volumeMusicSlider = new Slider(0.0f, 1.0f, 0.05f, false, skin);
        final Slider volumeSoundSlider = new Slider(0, 100, 5, false, skin);
        final TextButton returnToMenuScreen = new TextButton("Main menu", skin);
        final TextButton goToChangeControls = new TextButton("Change controls", skin);

        final Label titleLabel = new Label("Settings", skin);
        final Label volumeMusicLabel = new Label("Music volume", skin);
        final Label volumeSoundLabel = new Label("Sound volume", skin);

        volumeMusicIndicator = new Label(String.valueOf(Math.round(volumeMusicSlider.getValue() * 100)), skin);
        volumeSoundIndicator = new Label(String.valueOf(volumeSoundSlider.getValue()), skin);

        // Music.
        MusicPlayer.setMusic("soundfx/bensound-evolution.mp3");

        // Add the sliders and labels to the table.
        table.add(titleLabel).fillX().uniformX().pad(0, 0, 20, 0).bottom().right();

        table.row().pad(0, 0, 5, 30);
        table.add(volumeMusicLabel).fillX().uniformX();
        table.add(volumeMusicSlider).fillX().uniformX();
        table.add(volumeMusicIndicator).fillX().uniformX();

        table.row().pad(0, 0, 100, 30);
        table.add(volumeSoundLabel).fillX().uniformX();
        table.add(volumeSoundSlider).fillX().uniformX();
        table.add(volumeSoundIndicator).fillX().uniformX();

        table.row().pad(0, 0, 0, 30);
        table.add(returnToMenuScreen).fillX().uniformX().bottom().right();
        table.add(goToChangeControls).fillX().uniformX().bottom().right();

        table.pack();

        // Slider and button functionality.
        volumeMusicSlider.setValue(AppPreferences.getMusicVolume());
        volumeMusicSlider.addListener(event -> {
            AppPreferences.setMusicVolume(volumeMusicSlider.getValue());
            volumeMusicIndicator.setText(String.valueOf(Math.round(volumeMusicSlider.getValue() * 100)));
            return false;
        });

        volumeSoundSlider.setValue(AppPreferences.getSoundVolume());
        volumeSoundSlider.addListener(event -> {
            AppPreferences.setSoundVolume(volumeSoundSlider.getValue());
            volumeSoundIndicator.setText(String.valueOf(volumeSoundSlider.getValue()));
            return false;
        });

        returnToMenuScreen.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.clear();
                parent.changeScreen(VoShooter.Screen.MENU);
            }
        });

        goToChangeControls.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.clear();
                parent.changeScreen(VoShooter.Screen.CHANGE_CONTROLS);
            }
        });
    }

    /**
     * Render the elements specified in the show() method every frame.
     */
    @Override
    public void render(float delta) {
        // Refresh the graphics renderer every cycle.
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // And draw over it again.
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));  // Cap menu FPS to 30.
        MusicPlayer.setVolume(AppPreferences.getMusicVolume());
        stage.draw();
    }

    /**
     * Update to check if the window needs to be resized.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

}

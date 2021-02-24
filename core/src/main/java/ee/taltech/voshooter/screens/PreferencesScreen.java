package ee.taltech.voshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.voshooter.VoShooter;

public class PreferencesScreen implements Screen {

    private VoShooter parent;
    private Stage stage;

    /**
     * Construct the preferences screen. Pass in a reference to the orchestrator.
     * @param parent A reference to the orchestrator to enable communication.
     */
    public PreferencesScreen(VoShooter parent) {
        this.parent = parent;

        // Create stage which will contain this screen's objects
        stage = new Stage(new ScreenViewport());
    }

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
        final Slider volumeMusicSlider = new Slider(0f, 1f, 0.1f, false, skin);
        final Slider volumeSoundSlider = new Slider(0f, 1f, 0.1f, false, skin);
        final TextButton returnToMenuScreen = new TextButton("Main menu", skin);

        final Label titleLabel = new Label("Settings", skin);
        final Label volumeMusicLabel = new Label("Music volume", skin);
        final Label volumeSoundLabel = new Label("Sound volume", skin);

        // Add the sliders and labels to the table.
        table.add(titleLabel).fillX().uniformX().pad(0, 0, 20, 0);

        table.row().pad(0, 0, 5, 30);
        table.add(volumeMusicLabel).fillX().uniformX();
        table.add(volumeMusicSlider).fillX().uniformX();

        table.row().pad(0, 0, 100, 30);
        table.add(volumeSoundLabel).fillX().uniformX();
        table.add(volumeSoundSlider).fillX().uniformX();

        table.row();
        table.add(returnToMenuScreen).fillX().uniformX();

        // Slider and button functionality.
        volumeMusicSlider.setValue(parent.getPreferences().getMusicVolume());
        volumeMusicSlider.addListener(event -> {
            parent.getPreferences().setMusicVolume(volumeMusicSlider.getValue());
            return false;
        });

        volumeSoundSlider.setValue(parent.getPreferences().getSoundVolume());
        volumeSoundSlider.addListener(event -> {
            parent.getPreferences().setSoundVolume(volumeSoundSlider.getValue());
            return false;
        });

        returnToMenuScreen.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.clear();
                parent.changeScreen(VoShooter.Screen.MENU);
            }
        });
    }

    @Override
    public void render(float delta) {
        // Refresh the graphics renderer every cycle.
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // And draw over it again.
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));  // Cap menu FPS to 30.
        stage.draw();
    }

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
        // TODO Auto-generated method stub

    }

}

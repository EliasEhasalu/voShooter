package ee.taltech.voshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.voshooter.AppPreferences;
import ee.taltech.voshooter.VoShooter;
import ee.taltech.voshooter.settingsinputs.SettingsInput;
import ee.taltech.voshooter.settingsinputs.SettingsInputsHandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChangeControlsScreen implements Screen {

    private VoShooter parent;
    private Stage stage;
    private SettingsInputsHandler inputHandler;
    private InputMultiplexer inputMultiplexer;
    private Map<Label, TextButton> controls;
    private Label keyUp;
    private Label keyDown;
    private Label keyLeft;
    private Label keyRight;
    private Map.Entry<Label, TextButton> changeControlEntry;

    /**
     * Construct the preferences screen. Pass in a reference to the orchestrator.
     * @param parent A reference to the orchestrator to enable communication.
     */
    public ChangeControlsScreen(VoShooter parent) {
        this.parent = parent;

        // Create stage which will contain this screen's objects
        stage = new Stage(new ScreenViewport());
        inputHandler = new SettingsInputsHandler();
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(inputHandler);
    }

    /**
     * Initialize UI elements to be drawn.
     */
    @Override
    public void show() {
        // Have it handle player's input.
        Gdx.input.setInputProcessor(inputMultiplexer);
        // Add a table which will contain settings items to the stage.
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // A Skin object defines the theme for settings objects.
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        // Create the settings objects for our stage.
        final TextButton returnToPreferencesScreen = new TextButton("Back", skin);
        final Label titleLabel = new Label("Change controls", skin);

        // Control labels.
        controls = new LinkedHashMap<Label, TextButton>() {{
                keyUp = new Label("Move up", skin);
                put(keyUp, new TextButton(Input.Keys.toString(AppPreferences.getUpKey()), skin));
                keyDown = new Label("Move down", skin);
                put(keyDown, new TextButton(Input.Keys.toString(AppPreferences.getDownKey()), skin));
                keyLeft = new Label("Move left", skin);
                put(keyLeft, new TextButton(Input.Keys.toString(AppPreferences.getLeftKey()), skin));
                keyRight = new Label("Move right", skin);
                put(keyRight, new TextButton(Input.Keys.toString(AppPreferences.getRightKey()), skin));
            }};

        // Add the sliders and labels to the table.
        table.add(titleLabel).fillX().uniformX().pad(0, 0, 20, 0).bottom().right();

        for (Map.Entry<Label, TextButton> entry : controls.entrySet()) {
            table.row().pad(0, 0, 5, 30);
            table.add(entry.getKey());
            table.add(entry.getValue());
            entry.getValue().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (entry.getValue().getColor().equals(Color.RED)) {
                        entry.getValue().setColor(Color.WHITE);
                        changeControlEntry = null;
                    } else {
                        setButtonsWhite();
                        entry.getValue().setColor(Color.RED);
                        changeControlEntry = entry;
                    }
                }
            });
        }
        table.row().pad(0, 0, 100, 30);
        table.add(returnToPreferencesScreen).fillX().uniformX().bottom().right();

        table.pack();

        returnToPreferencesScreen.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.clear();
                changeControlEntry = null;
                parent.changeScreen(VoShooter.Screen.PREFERENCES);
            }
        });
    }

    /**
     * Set all the buttons back to white.
     */
    public void setButtonsWhite() {
        for (Map.Entry<Label, TextButton> entrySet : controls.entrySet()) {
            entrySet.getValue().setColor(Color.WHITE);
        }
    }

    /**
     * Render the elements specified in the show() method every frame.
     */
    @Override
    public void render(float delta) {
        // Refresh the graphics renderer every cycle.
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (changeControlEntry != null) {
            Integer inputKey = SettingsInput.getInputKey();
            if (inputKey != null) {
                Gdx.app.log("Input key", String.valueOf(inputKey));
                Gdx.app.log("Set key", String.valueOf(AppPreferences.getUpKey()));
                if (changeControlEntry.getKey().equals(keyUp)) {
                    AppPreferences.setUpKey(inputKey);
                    changeControlEntry.getValue().setText(Input.Keys.toString(AppPreferences.getUpKey()));
                    setButtonsWhite();
                    changeControlEntry = null;
                } else if (changeControlEntry.getKey().equals(keyDown)) {
                    AppPreferences.setDownKey(inputKey);
                    changeControlEntry.getValue().setText(Input.Keys.toString(AppPreferences.getDownKey()));
                    setButtonsWhite();
                    changeControlEntry = null;
                } else if (changeControlEntry.getKey().equals(keyLeft)) {
                    AppPreferences.setLeftKey(inputKey);
                    changeControlEntry.getValue().setText(Input.Keys.toString(AppPreferences.getLeftKey()));
                    setButtonsWhite();
                    changeControlEntry = null;
                } else if (changeControlEntry.getKey().equals(keyRight)) {
                    AppPreferences.setRightKey(inputKey);
                    changeControlEntry.getValue().setText(Input.Keys.toString(AppPreferences.getRightKey()));
                    setButtonsWhite();
                    changeControlEntry = null;
                }
            }
        }

        // And draw over it again.
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));  // Cap menu FPS to 30.
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

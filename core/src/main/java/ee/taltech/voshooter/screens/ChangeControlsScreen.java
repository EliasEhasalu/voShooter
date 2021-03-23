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

import java.util.HashMap;
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
    private Label buttonLeft;
    private Label buttonRight;
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
        controls = setButtonsCorrect();

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
     * Set the buttons to be buttons if buttons or keys if keys.
     * @return hashmap with correct buttons.
     */
    private HashMap<Label, TextButton> setButtonsCorrect() {
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        return new LinkedHashMap<Label, TextButton>() {{
            keyUp = new Label("Move up", skin);
            if (AppPreferences.getUpKeyIsKey()) {
                put(keyUp, new TextButton(Input.Keys.toString(AppPreferences.getUpKey()), skin));
            } else {
                put(keyUp, new TextButton(AppPreferences.stringRepresentation(AppPreferences.getUpKey()), skin));
            }
            keyLeft = new Label("Move left", skin);
            if (AppPreferences.getLeftKeyIsKey()) {
                put(keyLeft, new TextButton(Input.Keys.toString(AppPreferences.getLeftKey()), skin));
            } else {
                put(keyLeft, new TextButton(AppPreferences.stringRepresentation(AppPreferences.getLeftKey()), skin));
            }
            keyDown = new Label("Move down", skin);
            if (AppPreferences.getDownKeyIsKey()) {
                put(keyDown, new TextButton(Input.Keys.toString(AppPreferences.getDownKey()), skin));
            } else {
                put(keyDown, new TextButton(AppPreferences.stringRepresentation(AppPreferences.getDownKey()), skin));
            }
            keyRight = new Label("Move right", skin);
            if (AppPreferences.getRightKeyIsKey()) {
                put(keyRight, new TextButton(Input.Keys.toString(AppPreferences.getRightKey()), skin));
            } else {
                put(keyRight, new TextButton(AppPreferences.stringRepresentation(AppPreferences.getRightKey()), skin));
            }
            buttonLeft = new Label("Shoot", skin);
            if (AppPreferences.getButtonLeftIsKey()) {
                put(buttonLeft, new TextButton(Input.Keys.toString(AppPreferences.getMouseLeft()), skin));
            } else {
                put(buttonLeft, new TextButton(AppPreferences.stringRepresentation(AppPreferences.getMouseLeft()), skin));
            }
            buttonRight = new Label("Aim", skin);
            if (AppPreferences.getButtonRightIsKey()) {
                put(buttonRight, new TextButton(Input.Keys.toString(AppPreferences.getMouseRight()), skin));
            } else {
                put(buttonRight, new TextButton(AppPreferences.stringRepresentation(AppPreferences.getMouseRight()), skin));
            }
        }};
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
            Integer inputButton = SettingsInput.getInputButton();
            if (inputKey != null) {
                changeControlKey(inputKey);
            } else if (inputButton != null) {
                changeControlButton(inputButton);
            }
        }

        // And draw over it again.
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));  // Cap menu FPS to 30.
        stage.draw();
    }

    /**
     * Change control when a key clicked.
     * @param inputKey that was clicked
     */
    private void changeControlKey(Integer inputKey) {
        Label key = changeControlEntry.getKey();
        if (key.equals(keyUp)) {
            AppPreferences.setUpKey(inputKey);
            AppPreferences.setUpKeyIsKey(true);
        } else if (key.equals(keyDown)) {
            AppPreferences.setDownKey(inputKey);
            AppPreferences.setDownKeyIsKey(true);
        } else if (key.equals(keyLeft)) {
            AppPreferences.setLeftKey(inputKey);
            AppPreferences.setLeftKeyIsKey(true);
        } else if (key.equals(keyRight)) {
            AppPreferences.setRightKey(inputKey);
            AppPreferences.setRightKeyIsKey(true);
        } else if (key.equals(buttonLeft)) {
            AppPreferences.setMouseLeft(inputKey);
            AppPreferences.setButtonLeftIsKey(true);
        } else if (key.equals(buttonRight)) {
            AppPreferences.setMouseRight(inputKey);
            AppPreferences.setButtonRightIsKey(true);
        }
        changeControlEntry.getValue().setText(Input.Keys.toString(inputKey));
        setButtonsWhite();
        changeControlEntry = null;
    }

    /**
     * Change input when button clicked.
     * @param inputButton that was clicked
     */
    private void changeControlButton(Integer inputButton) {
        Label key = changeControlEntry.getKey();
        if (key.equals(keyUp)) {
            AppPreferences.setUpKey(inputButton);
            AppPreferences.setUpKeyIsKey(false);
        } else if (key.equals(keyDown)) {
            AppPreferences.setDownKey(inputButton);
            AppPreferences.setDownKeyIsKey(false);
        } else if (key.equals(keyLeft)) {
            AppPreferences.setLeftKey(inputButton);
            AppPreferences.setLeftKeyIsKey(false);
        } else if (key.equals(keyRight)) {
            AppPreferences.setRightKey(inputButton);
            AppPreferences.setRightKeyIsKey(false);
        } else if (key.equals(buttonLeft)) {
            AppPreferences.setMouseLeft(inputButton);
            AppPreferences.setButtonLeftIsKey(false);
        } else if (key.equals(buttonRight)) {
            AppPreferences.setMouseRight(inputButton);
            AppPreferences.setButtonRightIsKey(false);
        }
        changeControlEntry.getValue().setText(AppPreferences.stringRepresentation(inputButton));
        setButtonsWhite();
        changeControlEntry = null;
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

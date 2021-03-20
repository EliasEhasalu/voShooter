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
    private boolean keyUpIsKey = true;
    private Label keyDown;
    private boolean keyDownIsKey = true;
    private Label keyLeft;
    private boolean keyLeftIsKey = true;
    private Label keyRight;
    private boolean keyRightIsKey = true;
    private Label buttonLeft;
    private boolean buttonLeftIsKey = false;
    private Label buttonRight;
    private boolean buttonRightIsKey = false;
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
            if (keyUpIsKey) {
                put(keyUp, new TextButton(Input.Keys.toString(AppPreferences.getUpKey()), skin));
            } else {
                put(keyUp, new TextButton(AppPreferences.stringRepresentation(AppPreferences.getUpKey()), skin));
            }
            keyDown = new Label("Move down", skin);
            if (keyDownIsKey) {
                put(keyDown, new TextButton(Input.Keys.toString(AppPreferences.getDownKey()), skin));
            } else {
                put(keyDown, new TextButton(AppPreferences.stringRepresentation(AppPreferences.getDownKey()), skin));
            }
            keyLeft = new Label("Move left", skin);
            if (keyLeftIsKey) {
                put(keyLeft, new TextButton(Input.Keys.toString(AppPreferences.getLeftKey()), skin));
            } else {
                put(keyLeft, new TextButton(AppPreferences.stringRepresentation(AppPreferences.getLeftKey()), skin));
            }
            keyRight = new Label("Move right", skin);
            if (keyRightIsKey) {
                put(keyRight, new TextButton(Input.Keys.toString(AppPreferences.getRightKey()), skin));
            } else {
                put(keyRight, new TextButton(AppPreferences.stringRepresentation(AppPreferences.getRightKey()), skin));
            }
            buttonLeft = new Label("Shoot", skin);
            if (buttonLeftIsKey) {
                put(buttonLeft, new TextButton(Input.Keys.toString(AppPreferences.getMouseLeft()), skin));
            } else {
                put(buttonLeft, new TextButton(AppPreferences.stringRepresentation(AppPreferences.getMouseLeft()), skin));
            }
            buttonRight = new Label("Aim", skin);
            if (buttonRightIsKey) {
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
        if (changeControlEntry.getKey().equals(keyUp)) {
            AppPreferences.setUpKey(inputKey);
            changeControlEntry.getValue().setText(Input.Keys.toString(AppPreferences.getUpKey()));
            keyUpIsKey = true;
            setButtonsWhite();
            changeControlEntry = null;
        } else if (changeControlEntry.getKey().equals(keyDown)) {
            AppPreferences.setDownKey(inputKey);
            changeControlEntry.getValue().setText(Input.Keys.toString(AppPreferences.getDownKey()));
            keyDownIsKey = true;
            setButtonsWhite();
            changeControlEntry = null;
        } else if (changeControlEntry.getKey().equals(keyLeft)) {
            AppPreferences.setLeftKey(inputKey);
            changeControlEntry.getValue().setText(Input.Keys.toString(AppPreferences.getLeftKey()));
            keyLeftIsKey = true;
            setButtonsWhite();
            changeControlEntry = null;
        } else if (changeControlEntry.getKey().equals(keyRight)) {
            AppPreferences.setRightKey(inputKey);
            changeControlEntry.getValue().setText(Input.Keys.toString(AppPreferences.getRightKey()));
            keyRightIsKey = true;
            setButtonsWhite();
            changeControlEntry = null;
        } else if (changeControlEntry.getKey().equals(buttonLeft)) {
            AppPreferences.setMouseLeft(inputKey);
            changeControlEntry.getValue().setText(Input.Keys.toString(AppPreferences.getMouseLeft()));
            buttonLeftIsKey = true;
            setButtonsWhite();
            changeControlEntry = null;
        } else if (changeControlEntry.getKey().equals(buttonRight)) {
            AppPreferences.setMouseRight(inputKey);
            changeControlEntry.getValue().setText(Input.Keys.toString(AppPreferences.getMouseRight()));
            buttonRightIsKey = true;
            setButtonsWhite();
            changeControlEntry = null;
        }
    }

    /**
     * Change input when button clicked.
     * @param inputButton that was clicked
     */
    private void changeControlButton(Integer inputButton) {
        if (changeControlEntry.getKey().equals(keyUp)) {
            AppPreferences.setUpKey(inputButton);
            changeControlEntry.getValue().setText(AppPreferences.stringRepresentation(AppPreferences.getUpKey()));
            keyUpIsKey = false;
            setButtonsWhite();
            changeControlEntry = null;
        } else if (changeControlEntry.getKey().equals(keyDown)) {
            AppPreferences.setDownKey(inputButton);
            changeControlEntry.getValue().setText(AppPreferences.stringRepresentation(AppPreferences.getDownKey()));
            keyDownIsKey = false;
            setButtonsWhite();
            changeControlEntry = null;
        } else if (changeControlEntry.getKey().equals(keyLeft)) {
            AppPreferences.setLeftKey(inputButton);
            changeControlEntry.getValue().setText(AppPreferences.stringRepresentation(AppPreferences.getLeftKey()));
            keyLeftIsKey = false;
            setButtonsWhite();
            changeControlEntry = null;
        } else if (changeControlEntry.getKey().equals(keyRight)) {
            AppPreferences.setRightKey(inputButton);
            changeControlEntry.getValue().setText(AppPreferences.stringRepresentation(AppPreferences.getRightKey()));
            keyRightIsKey = false;
            setButtonsWhite();
            changeControlEntry = null;
        } else if (changeControlEntry.getKey().equals(buttonLeft)) {
            AppPreferences.setMouseLeft(inputButton);
            changeControlEntry.getValue().setText(AppPreferences.stringRepresentation(AppPreferences.getMouseLeft()));
            buttonLeftIsKey = false;
            setButtonsWhite();
            changeControlEntry = null;
        } else if (changeControlEntry.getKey().equals(buttonRight)) {
            AppPreferences.setMouseRight(inputButton);
            changeControlEntry.getValue().setText(AppPreferences.stringRepresentation(AppPreferences.getMouseRight()));
            buttonRightIsKey = false;
            setButtonsWhite();
            changeControlEntry = null;
        }
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

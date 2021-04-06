package ee.taltech.voshooter.controller;

import com.badlogic.gdx.Gdx;
import ee.taltech.voshooter.AppPreferences;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    /**
     * Get inputs of object and handle them.
     * @return ArrayList of keys and buttons currently pressed
     */
    public static List<ActionType> getInputs() {
        List<ActionType> actionsPerformed = new ArrayList<>();

        if ((Gdx.input.isKeyPressed(AppPreferences.getLeftKey()) && AppPreferences.getLeftKeyIsKey())
                || (Gdx.input.isButtonPressed(AppPreferences.getLeftKey()) && !AppPreferences.getLeftKeyIsKey())) {
            actionsPerformed.add(ActionType.MOVE_LEFT);
        }
        if ((Gdx.input.isKeyPressed(AppPreferences.getRightKey()) && AppPreferences.getRightKeyIsKey())
                || (Gdx.input.isButtonPressed(AppPreferences.getRightKey()) && !AppPreferences.getRightKeyIsKey())) {
            actionsPerformed.add(ActionType.MOVE_RIGHT);
        }
        if ((Gdx.input.isKeyPressed(AppPreferences.getUpKey()) && AppPreferences.getUpKeyIsKey())
                || (Gdx.input.isButtonPressed(AppPreferences.getUpKey()) && !AppPreferences.getUpKeyIsKey())) {
            actionsPerformed.add(ActionType.MOVE_UP);
        }
        if ((Gdx.input.isKeyPressed(AppPreferences.getDownKey()) && AppPreferences.getDownKeyIsKey())
                || (Gdx.input.isButtonPressed(AppPreferences.getDownKey()) && !AppPreferences.getDownKeyIsKey())) {
            actionsPerformed.add(ActionType.MOVE_DOWN);
        }
        // Mouse buttons
        if ((Gdx.input.isKeyPressed(AppPreferences.getMouseLeft()) && AppPreferences.getButtonLeftIsKey())
                || (Gdx.input.isButtonPressed(AppPreferences.getMouseLeft()) && !AppPreferences.getButtonLeftIsKey())) {
            actionsPerformed.add(ActionType.MOUSE_LEFT);
        }
        if ((Gdx.input.isKeyPressed(AppPreferences.getMouseRight()) && AppPreferences.getButtonRightIsKey())
                || (Gdx.input.isButtonPressed(AppPreferences.getMouseRight()) && !AppPreferences.getButtonRightIsKey())) {
            actionsPerformed.add(ActionType.MOUSE_RIGHT);
        }

        return actionsPerformed;
    }
}

package ee.taltech.voshooter.controller;

import com.badlogic.gdx.Gdx;
import ee.taltech.voshooter.AppPreferences;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerAction;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    /**
     * Get inputs of object and handle them.
     * @return ArrayList of keys and buttons currently pressed
     */
    public static List<ActionType> getInputs() {
        List<ActionType> actionsPerformed = new ArrayList<>();

        if (Gdx.input.isKeyPressed(AppPreferences.getLeftKey())) {
            actionsPerformed.add(ActionType.MOVE_LEFT);
        }
        if (Gdx.input.isKeyPressed(AppPreferences.getRightKey())) {
            actionsPerformed.add(ActionType.MOVE_RIGHT);
        }
        if (Gdx.input.isKeyPressed(AppPreferences.getUpKey())) {
            actionsPerformed.add(ActionType.MOVE_UP);
        }
        if (Gdx.input.isKeyPressed(AppPreferences.getDownKey())) {
            actionsPerformed.add(ActionType.MOVE_DOWN);
        }
        // Mouse buttons
        if (Gdx.input.isButtonPressed(AppPreferences.getMouseLeft())) {
            actionsPerformed.add(ActionType.MOUSE_LEFT);
        }
        if (Gdx.input.isButtonPressed(AppPreferences.getMouseRight())) {
            actionsPerformed.add(ActionType.MOUSE_RIGHT);
        }

        return actionsPerformed;
    }
}

package ee.taltech.voshooter.controller;

import com.badlogic.gdx.Gdx;
import ee.taltech.voshooter.AppPreferences;

import java.util.ArrayList;

public class GameController {

    /**
     * Get inputs of object and handle them.
     * @return ArrayList of keys and buttons currently pressed
     */
    public static ArrayList<PlayerAction> getInputs() {
        ArrayList<PlayerAction> pressedKeys = new ArrayList<>();

        if (Gdx.input.isKeyPressed(AppPreferences.getLeftKey())) {
            pressedKeys.add(PlayerAction.MOVE_LEFT);
        }
        if (Gdx.input.isKeyPressed(AppPreferences.getRightKey())) {
            pressedKeys.add(PlayerAction.MOVE_RIGHT);
        }
        if (Gdx.input.isKeyPressed(AppPreferences.getUpKey())) {
            pressedKeys.add(PlayerAction.MOVE_UP);
        }
        if (Gdx.input.isKeyPressed(AppPreferences.getDownKey())) {
            pressedKeys.add(PlayerAction.MOVE_DOWN);
        }
        // Mouse buttons
        if (Gdx.input.isButtonPressed(AppPreferences.getMouseLeft())) {
            pressedKeys.add(PlayerAction.MOUSE_LEFT);
        }
        if (Gdx.input.isButtonPressed(AppPreferences.getMouseRight())) {
            pressedKeys.add(PlayerAction.MOUSE_RIGHT);
        }
        return pressedKeys;
    }
}

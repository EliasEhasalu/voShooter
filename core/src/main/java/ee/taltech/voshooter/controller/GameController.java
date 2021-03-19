package ee.taltech.voshooter.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    /**
     * Get inputs of object and handle them.
     * @return ArrayList of keys and buttons currently pressed
     */
    public static List<PlayerAction> getInputs() {
        List<PlayerAction> pressedKeys = new ArrayList<>();

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            pressedKeys.add(PlayerAction.MOVE_LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            pressedKeys.add(PlayerAction.MOVE_RIGHT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            pressedKeys.add(PlayerAction.MOVE_UP);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            pressedKeys.add(PlayerAction.MOVE_DOWN);
        }
        // Mouse buttons
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            pressedKeys.add(PlayerAction.MOUSE_LEFT);
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            pressedKeys.add(PlayerAction.MOUSE_RIGHT);
        }
        return pressedKeys;
    }
}

package ee.taltech.voshooter.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL30;

import java.util.ArrayList;

public class GameController {

    /**
     * Get inputs of object and handle them.
     * @return ArrayList of keys and buttons currently pressed
     */
    public static ArrayList<Integer> getInputs() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        ArrayList<Integer> pressedKeys = new ArrayList<>();

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            pressedKeys.add(Input.Keys.A);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            pressedKeys.add(Input.Keys.D);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            pressedKeys.add(Input.Keys.W);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            pressedKeys.add(Input.Keys.S);
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            pressedKeys.add(Input.Buttons.LEFT);
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            pressedKeys.add(Input.Buttons.RIGHT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            pressedKeys.add(Input.Keys.LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            pressedKeys.add(Input.Keys.RIGHT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            pressedKeys.add(Input.Keys.UP);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            pressedKeys.add(Input.Keys.DOWN);
        }
        return pressedKeys;
    }
}

package ee.taltech.voshooter.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL30;

public class GameController {

    /**
     * Get inputs of object and handle them.
     */
    public static void getInputs() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            System.out.println("A");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            System.out.println("D");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            System.out.println("W");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            System.out.println("S");
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            System.out.println("MOUSE LEFT");
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            System.out.println("MOUSE RIGHT");
        }
    }
}

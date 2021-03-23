package ee.taltech.voshooter.settingsinputs;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputProcessor;

public class SettingsInputsHandler implements InputProcessor, ApplicationListener {

    /**
     * Key is pressed.
     * @param k the key that was pressed
     * @return boolean ignored
     */
    @Override
    public boolean keyDown(int k) {
        System.out.println(k);
        SettingsInput.setInputKey(k);
        return true;
    }

    /**
     * Key is unpressed.
     * @param k that was unpressed
     * @return boolean ignored
     */
    @Override
    public boolean keyUp(int k) {
        SettingsInput.removeInputKey();
        return true;
    }

    /**
     * Key down didn't register, so using this as keydown in settings menu.
     * @param character that was typed
     * @return boolean ignored
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Mouse button is pressed.
     * @param screenX mouse x coordinate
     * @param screenY mouse y coordinate
     * @param pointer pointer
     * @param button that was clicked
     * @return boolean ignored
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println(button);
        SettingsInput.setInputButton(button);
        return true;
    }

    /**
     * Mouse button is released.
     * @param screenX mouse x coordinate
     * @param screenY mouse y coordinate
     * @param pointer pointer
     * @param button that was clicked
     * @return boolean ignored
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        SettingsInput.removeInputButton();
        return true;
    }

    /**
     * Placeholder.
     * @param screenX Placeholder.
     * @param screenY Placeholder.
     * @param pointer Placeholder.
     * @return Placeholder.
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Placeholder.
     * @param screenX Placeholder.
     * @param screenY Placeholder.
     * @return Placeholder.
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Placeholder.
     * @param amountX Placeholder.
     * @param amountY Placeholder.
     * @return Placeholder.
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void create() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void render() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}

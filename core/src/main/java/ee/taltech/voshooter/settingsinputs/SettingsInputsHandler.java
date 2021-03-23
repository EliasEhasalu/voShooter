package ee.taltech.voshooter.settingsinputs;

import com.badlogic.gdx.InputProcessor;

public class SettingsInputsHandler implements InputProcessor {

    /**
     * Key is pressed.
     * @param k the key that was pressed
     * @return boolean ignored
     */
    @Override
    public boolean keyDown(int k) {
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
     * Placeholder.
     * @param character Placeholder.
     * @return Placeholder.
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
}

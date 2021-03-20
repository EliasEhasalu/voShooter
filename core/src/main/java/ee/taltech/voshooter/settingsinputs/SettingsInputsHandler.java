package ee.taltech.voshooter.settingsinputs;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputAdapter;

public class SettingsInputsHandler extends InputAdapter implements ApplicationListener {

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

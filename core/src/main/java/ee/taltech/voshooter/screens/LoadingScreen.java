package ee.taltech.voshooter.screens;

import com.badlogic.gdx.Screen;

import ee.taltech.voshooter.VoShooter;

public class LoadingScreen implements Screen {

    private VoShooter parent;

    /**
     * Construct the loading screen. Pass in a reference to the orchestrator.
     * @param parent A reference to the orchestrator to enable communication.
     */
    public LoadingScreen(VoShooter parent) {
        this.parent = parent;
    }

    /**
     * When ready, set screen to menu.
     */
    @Override
    public void show() {
    }

    /**
     * When ready, set screen to menu.
     */
    @Override
    public void render(float delta) {
        parent.changeScreen(VoShooter.Screen.MENU);
    }

    @Override
    public void resize(int width, int height) {
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

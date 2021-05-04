package ee.taltech.voshooter.gamestate.gamemode;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.voshooter.screens.MainScreen;

public class ClientFunkyManager extends ClientGameModeManager {

    public static final float ROTATION_SPEED = 0.5f;
    private final OrthographicCamera camera;

    public ClientFunkyManager(OrthographicCamera camera, MainScreen screen, SpriteBatch batch) {
        super(screen, batch);
        this.camera = camera;
    }

    @Override
    public void update() {
        camera.rotate(ROTATION_SPEED);
        super.update();
    }

    public void setTimePassed(double timePassed) {
        this.timePassed = timePassed;
    }
}

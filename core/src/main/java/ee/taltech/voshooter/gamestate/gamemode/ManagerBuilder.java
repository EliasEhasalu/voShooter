package ee.taltech.voshooter.gamestate.gamemode;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.voshooter.screens.MainScreen;

public abstract class ManagerBuilder {

    public static ClientGameModeManager getGameModeManager(int gameMode, OrthographicCamera camera,
                                                    MainScreen screen, SpriteBatch batch) {
        if (gameMode == 0) return new ClientFunkyManager(camera, screen, batch);
        if (gameMode == 2) return new ClientKingOfTheHillManager(screen, batch);
        else return new ClientGameModeManager(screen, batch);
    }
}

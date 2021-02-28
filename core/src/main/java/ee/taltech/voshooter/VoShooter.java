package ee.taltech.voshooter;


import com.badlogic.gdx.Game;

import ee.taltech.voshooter.networking.VoClient;
import ee.taltech.voshooter.screens.CreateGameScreen;
import ee.taltech.voshooter.screens.JoinGameScreen;
import ee.taltech.voshooter.screens.LoadingScreen;
import ee.taltech.voshooter.screens.MainScreen;
import ee.taltech.voshooter.screens.MenuScreen;
import ee.taltech.voshooter.screens.PreferencesScreen;

public class VoShooter extends Game {

    private LoadingScreen loadingScreen;
    private PreferencesScreen preferencesScreen;
    private MenuScreen menuScreen;
    private MainScreen mainScreen;
    private AppPreferences preferences;
    private CreateGameScreen createGameScreen;
    private JoinGameScreen joinGameScreen;
    public VoClient client;

    public enum Screen {
        LOADING,
        MENU,
        PREFERENCES,
        MAIN,
        CREATE_GAME,
        JOIN_GAME
    }

    /**
     * Initialize the orchestrator object.
     */
    @Override
    public void create() {
        preferences = new AppPreferences();
        changeScreen(VoShooter.Screen.LOADING);
        client = new VoClient();
    }

    /**
     * Change the current screen.
     * @param screen An enumerable of type Screen.
     */
    public void changeScreen(Screen screen) {
        switch (screen) {
            case MENU:
                if (menuScreen == null) menuScreen = new MenuScreen(this);
                setScreen(menuScreen);
                break;
            case PREFERENCES:
                if (preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);
                setScreen(preferencesScreen);
                break;
            case MAIN:
                if (mainScreen == null) mainScreen = new MainScreen(this);
                setScreen(mainScreen);
                break;
            case LOADING:
                if (loadingScreen == null) loadingScreen = new LoadingScreen(this);
                setScreen(loadingScreen);
                break;
            case CREATE_GAME:
                if (createGameScreen == null) createGameScreen = new CreateGameScreen(this);
                setScreen(createGameScreen);
                break;
            case JOIN_GAME:
                if (joinGameScreen == null) joinGameScreen = new JoinGameScreen(this);
                setScreen(joinGameScreen);
                break;
            default:
                // Noop.
        }
    }

    /**
    * @return The object defining the user's app preferences.
    */
    public AppPreferences getPreferences() {
        return preferences;
    }
}

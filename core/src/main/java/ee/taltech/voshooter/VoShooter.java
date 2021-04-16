package ee.taltech.voshooter;


import com.badlogic.gdx.Game;
import com.esotericsoftware.kryonet.Client;
import ee.taltech.voshooter.gamestate.GameState;
import ee.taltech.voshooter.networking.VoClient;
import ee.taltech.voshooter.screens.ChangeControlsScreen;
import ee.taltech.voshooter.screens.CreateGameScreen;
import ee.taltech.voshooter.screens.JoinGameScreen;
import ee.taltech.voshooter.screens.LoadingScreen;
import ee.taltech.voshooter.screens.LobbyScreen;
import ee.taltech.voshooter.screens.MainScreen;
import ee.taltech.voshooter.screens.MenuScreen;
import ee.taltech.voshooter.screens.PreferencesScreen;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class VoShooter extends Game {

    private LoadingScreen loadingScreen;
    private PreferencesScreen preferencesScreen;
    private MenuScreen menuScreen;
    public MainScreen mainScreen;
    private AppPreferences preferences;
    private ChangeControlsScreen changeControlsScreen;
    public CreateGameScreen createGameScreen;
    public JoinGameScreen joinGameScreen;
    private LobbyScreen lobbyScreen;
    private boolean codeCorrect;
    private String lobbyCode;
    private boolean cameFromGame;
    public VoClient client;
    public GameState gameState;
    private Screen screen;

    public String HOST_ADDRESS = "193.40.255.29";
    public int PORT_NUMBER = 5001;

    public enum Screen {
        LOADING,
        MENU,
        PREFERENCES,
        MAIN,
        CREATE_GAME,
        JOIN_GAME,
        LOBBY,
        CHANGE_CONTROLS
    }

    public VoShooter(String[] args) {
       if (args.length > 0) HOST_ADDRESS = args[0];
       if (args.length > 1) PORT_NUMBER = Integer.parseInt(args[1]);
    }

    /**
     * Initialize the orchestrator object.
     */
    @Override
    public void create() {
        preferences = new AppPreferences();
        gameState = new GameState();
        changeScreen(VoShooter.Screen.LOADING);
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
                this.screen = screen;
                break;
            case PREFERENCES:
                if (preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);
                setScreen(preferencesScreen);
                this.screen = screen;
                break;
            case MAIN:
                if (mainScreen == null) mainScreen = new MainScreen(this);
                if (this.screen != screen) {
                    setScreen(mainScreen);
                    this.screen = screen;
                }
                break;
            case LOADING:
                if (loadingScreen == null) loadingScreen = new LoadingScreen(this);
                setScreen(loadingScreen);
                this.screen = screen;
                break;
            case CREATE_GAME:
                if (createGameScreen == null) createGameScreen = new CreateGameScreen(this);
                setScreen(createGameScreen);
                this.screen = screen;
                break;
            case JOIN_GAME:
                if (joinGameScreen == null) joinGameScreen = new JoinGameScreen(this);
                setScreen(joinGameScreen);
                this.screen = screen;
                break;
            case LOBBY:
                if (lobbyScreen == null) lobbyScreen = new LobbyScreen(this);
                setScreen(lobbyScreen);
                this.screen = screen;
                break;
            case CHANGE_CONTROLS:
                if (changeControlsScreen == null) changeControlsScreen = new ChangeControlsScreen(this);
                setScreen(changeControlsScreen);
                this.screen = screen;
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

    /**
     * @return The client where you can send outbound requests.
     */
    public Client getClient() {
        return client.client;
    }

    /**
     * Get the code correct boolean.
     * @return boolean
     */
    public boolean isCodeCorrect() {
        return codeCorrect;
    }

    /**
     * Set a boolean if the code was incorrect.
     * @param codeCorrect boolean
     */
    public void setCodeCorrect(boolean codeCorrect) {
        this.codeCorrect = codeCorrect;
    }

    /**
     * @return code of the attempted lobby.
     */
    public String getLobbyCode() {
        return lobbyCode;
    }

    /**
     * @param lobbyCode Code of the lobby.
     */
    public void setLobbyCode(String lobbyCode) {
        this.lobbyCode = lobbyCode;
    }

    /**
     * @return true if previous screen was main screen.
     */
    public boolean isCameFromGame() {
        return cameFromGame;
    }

    /**
     * @param cameFromGame set true if came from main screen.
     */
    public void setCameFromGame(boolean cameFromGame) {
        this.cameFromGame = cameFromGame;
    }

    /**
     * Used to instantiate a new VoClient object for communication
     * with the server.
     */
    public void createNetworkClient() throws IOException {
        client = new VoClient(this, HOST_ADDRESS, PORT_NUMBER);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ignored) {
        }
    }
}

package ee.taltech.voshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.voshooter.VoShooter;
import ee.taltech.voshooter.networking.messages.User;
import ee.taltech.voshooter.networking.messages.serverreceived.LeaveLobby;
import ee.taltech.voshooter.networking.messages.serverreceived.StartGame;
import ee.taltech.voshooter.soundeffects.MusicPlayer;

import java.util.ArrayList;
import java.util.List;

import static ee.taltech.voshooter.VoShooter.Screen.LOBBY_SETTINGS;
import static ee.taltech.voshooter.VoShooter.Screen.MENU;

public class LobbyScreen implements Screen {

    private static final String EMPTY_SLOT = "|            |";

    private VoShooter parent;
    private Stage stage;
    private Label gamemodeLabel;
    private Label mapLabel;
    private Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
    private TextButton settingsButton = new TextButton("Settings", skin);
    private TextButton startGame = new TextButton("Start", skin);
    private List<Label> playerNameLabels = new ArrayList<>();
    private Label lobbyCodeLabel;
    private Table table = new Table();
    private Table playerNameTable;

    /**
     * Construct the menu screen.
     * @param parent A reference to the orchestrator object.
     */
    public LobbyScreen(VoShooter parent) {
        this.parent = parent;

        // Create stage which will contain this screen's objects
        stage = new Stage(new ScreenViewport());
    }

    /**
     * Define the assets to be shown in the render loop.
     */
    @Override
    public void show() {
        // Have it handle player's input.
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        // A Skin object defines the theme for menu objects.

        // Add a table which will contain game creation settings.
        table.clear();
        table.setFillParent(true);
        stage.addActor(table);

        // Create the menu objects for our stage.
        Label lobbyTitleLabel = new Label("Lobby", skin);
        lobbyCodeLabel = new Label(parent.gameState.currentLobby.getLobbyCode(), skin);
        mapLabel = new Label("Map: " + parent.gameState.currentLobby.getMap().name(), skin);
        int gameMode = parent.gameState.currentLobby.getGamemode();
        gamemodeLabel = new Label("Gamemode: " + parent.lobbySettingsScreen.gameModes.get(gameMode), skin);
        TextButton leaveButton = new TextButton("Leave", skin);
        if (!parent.gameState.clientUser.isHost()) {
            settingsButton.setVisible(false);
            startGame.setVisible(false);
        }

        playerNameLabels.clear();
        playerNameTable = new Table();

        // Add the objects to the table.
        table.add(lobbyTitleLabel);
        table.add(lobbyCodeLabel);
        table.row().pad(10, 0, 0, 0);
        table.add(mapLabel).left();
        table.add(settingsButton).right();
        table.row().pad(10, 0, 0, 0);
        table.add(gamemodeLabel).left();
        table.row().pad(40, 0, 0, 0);
        table.add(playerNameTable).left();
        table.row().pad(50, 0, 0, 0);
        table.add(leaveButton).left();
        table.add(startGame).right();

        // Music.
        MusicPlayer.setMusic("soundfx/bensound-theelevatorbossanova.mp3");

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    leaveButton.toggle();
                } else if (keycode == Input.Keys.ENTER) {
                    startGame.toggle();
                }
                return true;
            }
        });

        if (parent.doesNotContainChangeListener(settingsButton.getListeners())) {
            settingsButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (parent.gameState.clientUser.isHost()) {
                        parent.changeScreen(LOBBY_SETTINGS);
                    }
                }
            });
        }

        // Add button functionality.
        if (parent.doesNotContainChangeListener(leaveButton.getListeners())) {
            leaveButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    parent.gameState.currentLobby.clearLobby();
                    parent.gameState.clientUser.setHost(false);
                    playerNameLabels.clear();
                    parent.getClient().sendTCP(new LeaveLobby());
                    parent.changeScreen(MENU);
                }
            });
        }

        if (parent.doesNotContainChangeListener(startGame.getListeners())) {
            startGame.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (parent.gameState.clientUser.isHost()) {
                        playerNameLabels.clear();
                        parent.getClient().sendTCP(new StartGame());
                    }
                }
            });
        }
    }

    private void updateLobbyScreen() {
        int maxPlayers = parent.gameState.currentLobby.getMaxUsers();
        mapLabel.setText("Map: " + parent.gameState.currentLobby.getMap().name());
        int gameMode = parent.gameState.currentLobby.getGamemode();
        gamemodeLabel.setText("Gamemode: " + parent.lobbySettingsScreen.gameModes.get(gameMode));
        playerNameLabels.clear();
        playerNameTable.clear();
        for (int i = 0; i < maxPlayers; i++) {
            Label playerName = new Label(LobbyScreen.EMPTY_SLOT, skin);
            playerNameLabels.add(playerName);
            playerNameTable.add(playerName).left();
            playerNameTable.row().pad(10, 0, 0, 0);
        }
    }

    /**
     * Render the elements defined in the show() method.
     */
    @Override
    public void render(float delta) {
        // Update lobby.
        updateLobbyScreen();
        int maxPlayers = parent.gameState.currentLobby.getMaxUsers();
        int joinedPlayers = parent.gameState.currentLobby.getUsersCount();

        for (int i = 0; i < maxPlayers; i++) {
            if (i < joinedPlayers && playerNameLabels.size() > i) {
                User user = parent.gameState.currentLobby.getUsers().get(i);
                if (user.isHost()) {
                    playerNameLabels.get(i).setText(user.getName() + "   < Host");
                    if (parent.gameState.clientUser.id == user.id) {
                        playerNameLabels.get(i).setText(user.getName() + "   < Host/You");
                        parent.gameState.clientUser.setHost(true);
                    }
                } else if (parent.gameState.clientUser.id == user.id) playerNameLabels.get(i)
                        .setText(user.getName() + "   < You");
                else playerNameLabels.get(i).setText(user.getName());
            }
        }

        if (parent.gameState.ongoingGame) {
            parent.changeScreen(VoShooter.Screen.MAIN);
        }

        if (parent.gameState.clientUser.isHost()) {
            settingsButton.setVisible(true);
            startGame.setVisible(true);
        }

        // Refresh the graphics renderer every cycle.
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // And draw over it again.
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));  // Cap menu FPS to 30.
        stage.draw();
    }

    /**
     * Make sure the window doesn't break.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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

    /**
     * Dispose of the screen.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}

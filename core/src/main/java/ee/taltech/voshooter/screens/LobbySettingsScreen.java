package ee.taltech.voshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.voshooter.VoShooter;
import ee.taltech.voshooter.map.GameMap;
import ee.taltech.voshooter.networking.messages.serverreceived.LobbySettingsChanged;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LobbySettingsScreen implements Screen {

    private VoShooter parent;
    private Stage stage;
    private int playerCount = 4;
    private int gameMode = 1;
    private int roundLength = 0;
    private int botAmount = 0;
    private boolean isCorrectGameLength = false;
    private GameMap.MapType mapType = GameMap.PLAYER_MAPS[0];
    public Map<Integer, String> gameModes = new HashMap<Integer, String>() {{
        put(0, "Funky");
        put(1, "FFA");
        put(2, "KotH");
    }};

    /**
     * Construct the menu screen.
     * @param parent A reference to the orchestrator object.
     */
    public LobbySettingsScreen(VoShooter parent) {
        this.parent = parent;
        // Create stage which will contain this screen's objects
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        // Have it handle player's input.
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        // A Skin object defines the theme for menu objects.
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        // Add a table which will contain game creation settings.
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        updateLobbySettings();

        TextButton cancel = new TextButton("Cancel", skin);
        TextButton save = new TextButton("Save changes", skin);
        Table gameModeTable = new Table();
        Label gameModeLabel = new Label("Gamemode:", skin);
        Label gameModeLabel2 = new Label(gameModes.get(gameMode), skin);
        TextButton gameModeDecrease = new TextButton("<", skin);
        TextButton gameModeIncrease = new TextButton(">", skin);
        Table playerCountTable = new Table();
        Label playersLabel = new Label("Players:", skin);
        Label playerCountLabel = new Label(String.valueOf(playerCount), skin);
        TextButton playerCountDecrease = new TextButton("<", skin);
        TextButton playerCountIncrease = new TextButton(">", skin);
        Label mapLabel = new Label("Map", skin);
        TextButton changeMapButton = new TextButton(mapType.name(), skin);
        Label gameLength = new Label("Round length: ", skin);
        TextField gameLengthField = new TextField("", skin);
        String gameLengthString = String.valueOf(parent.gameState.currentLobby.getGameLength());
        gameLengthField.setText(gameLengthString);
        Label gameLengthHint = new Label("seconds", skin);
        if (parent.gameState.currentLobby.getGameLength() < 15) gameLengthHint.setText("endless");
        Table botTable = new Table(skin);
        Label botLabel = new Label("Bots:", skin);
        Label botLabel2 = new Label(String.valueOf(botAmount), skin);
        TextButton botCountDecrease = new TextButton("<", skin);
        TextButton botCountIncrease = new TextButton(">", skin);

        table.add(playersLabel).left();
        table.add(playerCountTable).fillX();
        playerCountTable.add(playerCountDecrease).left();
        playerCountTable.add(playerCountLabel).center().fillX();
        playerCountTable.add(playerCountIncrease).right();
        table.row().pad(10, 0, 0, 0);
        table.add(gameModeLabel).left();
        gameModeTable.add(gameModeDecrease).left();
        gameModeTable.add(gameModeLabel2).center().fillX();
        gameModeTable.add(gameModeIncrease).right();
        table.add(gameModeTable).center();
        table.row().pad(10, 0, 0, 0);
        table.add(mapLabel).left();
        table.add(changeMapButton).center();
        table.row().pad(10, 0, 0, 0);
        table.add(gameLength).left();
        table.add(gameLengthField).center();
        table.add(gameLengthHint).right();
        table.row().pad(10, 0, 0, 0);
        table.add(botLabel).left();
        botTable.add(botCountDecrease).left();
        botTable.add(botLabel2).center().fillX();
        botTable.add(botCountIncrease).right();
        table.add(botTable);
        table.row().pad(100, 0, 0, 0);
        table.add(cancel).left().padRight(10);
        table.add(save).right();

        if (parent.doesNotContainChangeListener(cancel.getListeners())) {
            cancel.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    parent.changeScreen(VoShooter.Screen.LOBBY);
                }
            });
        }

        if (parent.doesNotContainChangeListener(save.getListeners())) {
            save.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    parent.getClient().sendTCP(new LobbySettingsChanged(gameMode, mapType,
                            Math.max(playerCount, parent.gameState.currentLobby.getUsersCount()),
                            parent.gameState.currentLobby.getLobbyCode(), roundLength, botAmount));
                    parent.changeScreen(VoShooter.Screen.LOBBY);
                }
            });
        }

        if (parent.doesNotContainChangeListener(playerCountDecrease.getListeners())) {
            playerCountDecrease.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (playerCount > 2) {
                        playerCount--;
                        playerCountLabel.setText(String.valueOf(playerCount));
                    }
                }
            });
        }

        if (parent.doesNotContainChangeListener(playerCountIncrease.getListeners())) {
            playerCountIncrease.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (playerCount < 8) {
                        playerCount++;
                        playerCountLabel.setText(String.valueOf(playerCount));
                    }
                }
            });
        }

        if (parent.doesNotContainChangeListener(botCountDecrease.getListeners())) {
            botCountDecrease.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (botAmount > 0) {
                        botAmount--;
                        botLabel2.setText(String.valueOf(botAmount));
                    }
                }
            });
        }

        if (parent.doesNotContainChangeListener(botCountIncrease.getListeners())) {
            botCountIncrease.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (botAmount < 8) {
                        botAmount++;
                        botLabel2.setText(String.valueOf(botAmount));
                    }
                }
            });
        }

        if (parent.doesNotContainChangeListener(gameModeDecrease.getListeners())) {
            gameModeDecrease.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (gameMode > 0) {
                        gameMode--;
                        gameModeLabel2.setText(gameModes.get(gameMode));
                    }
                }
            });
        }

        if (parent.doesNotContainChangeListener(gameModeIncrease.getListeners())) {
            gameModeIncrease.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (gameMode < gameModes.size() - 1) {
                        gameMode++;
                        gameModeLabel2.setText(gameModes.get(gameMode));
                    }
                }
            });
        }

        if (parent.doesNotContainChangeListener(changeMapButton.getListeners())) {
            changeMapButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    int i = Arrays.asList(GameMap.PLAYER_MAPS).indexOf(mapType);
                    i++;
                    if (i >= GameMap.PLAYER_MAPS.length) i = 0;
                    mapType = GameMap.PLAYER_MAPS[i];
                    changeMapButton.setText(mapType.name());
                }
            });
        }

        if (parent.doesNotContainChangeListener(gameLengthField.getListeners())) {
            gameLengthField.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (!gameLengthField.getText().matches("^[0-9]*$") || gameLengthField.getText().length() <= 0) {
                        gameLengthHint.setText("Only numbers 0-9");
                        gameLengthHint.setColor(255, 0, 0, 255);
                        isCorrectGameLength = false;
                    } else {
                        if (Integer.parseInt(gameLengthField.getText()) >= 15) gameLengthHint.setText("seconds");
                        else gameLengthHint.setText("endless");
                        gameLengthHint.setColor(0, 255, 0, 255);
                        roundLength = Integer.parseInt(gameLengthField.getText());
                        isCorrectGameLength = true;
                    }
                }
            });
        }
    }

    private void updateLobbySettings() {
        playerCount = parent.gameState.currentLobby.getMaxUsers();
        gameMode = parent.gameState.currentLobby.getGamemode();
        roundLength = parent.gameState.currentLobby.getGameLength();
        botAmount = parent.gameState.currentLobby.getBotAmount();
        mapType = parent.gameState.currentLobby.getMap();
    }

    @Override
    public void render(float delta) {
        // Refresh the graphics renderer every cycle.
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // And draw over it again.
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));  // Cap menu FPS to 30.
        stage.draw();
    }

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

    @Override
    public void dispose() {
        stage.dispose();
    }
}

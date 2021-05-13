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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.voshooter.VoShooter;
import ee.taltech.voshooter.networking.messages.serverreceived.JoinLobby;
import ee.taltech.voshooter.networking.messages.serverreceived.SetUsername;

import java.io.IOException;


public class JoinGameScreen implements Screen {

    private VoShooter parent;
    private Stage stage;
    private TextButton join;
    private Table popUpTable;
    private TextField gameCode;
    private Label nameLengthCheck;
    private boolean isNameGood = false;
    private Label gameCodeCheck;
    private boolean isCodeGood = false;
    public VoShooter.Screen shouldChangeScreen;

    /**
     * Construct the menu screen.
     * @param parent A reference to the orchestrator object.
     */
    public JoinGameScreen(VoShooter parent) {
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
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        // Add a table which will contain game creation settings.
        Table table = new Table();
        Table bottomTable = new Table();
        table.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        table.setVisible(true);
        stage.addActor(table);

        // Table for no connection pop-up.
        popUpTable = new Table();
        stage.addActor(popUpTable);
        popUpTable.setVisible(false);
        popUpTable.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        Label noConnection = new Label("Connection failed, try again.", skin);
        TextButton closePopUp = new TextButton("Okay", skin);
        popUpTable.add(noConnection);
        popUpTable.row().pad(10, 0, 0, 0);
        popUpTable.add(closePopUp);

        join = new TextButton("Join lobby", skin);
        TextButton back = new TextButton("Back", skin);

        // Player name field and label.
        Table nameTable = new Table();
        Label enterName = new Label("Enter your username: ", skin);
        TextField playerName = new TextField("", skin);
        stage.setKeyboardFocus(playerName);
        playerName.setMaxLength(12);
        nameLengthCheck = new Label("Too short", skin);
        nameLengthCheck.setColor(255, 0, 0, 255);

        // Lobby code field and label.
        Table codeTable = new Table();
        Label enterCode = new Label("Enter lobby code: ", skin);
        gameCode = new TextField("", skin);
        gameCode.setMaxLength(6);
        gameCodeCheck = new Label("Too short", skin);
        gameCodeCheck.setColor(255, 0, 0, 255);

        // Add name label and field to table.
        nameTable.add(enterName).left().pad(0, 0, 0, 10);
        nameTable.add(playerName).right().width(210);
        table.add(nameTable).fill();
        table.row().pad(10, 0, 0, 0);
        table.add(nameLengthCheck).fill();
        nameLengthCheck.setAlignment(Align.center);
        table.row().pad(10, 0, 0, 0);

        // Add code label and field to table.
        codeTable.add(enterCode).left().pad(0, 0, 0, 10);
        codeTable.add(gameCode).right().width(115);
        table.add(codeTable).fill();
        table.row().pad(10, 0, 0, 0);
        table.add(gameCodeCheck).fill();
        gameCodeCheck.setAlignment(Align.center);
        table.row().pad(10, 0, 0, 0);

        // Join game button
        table.add(join).fill().maxWidth(200);
        table.row().pad(10, 0, 0, 0);
        bottomTable.add(back).left().pad(0, 0, 0, 10).maxWidth(200);
        table.add(bottomTable).fill().maxWidth(200);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    if (popUpTable.isVisible()) {
                        closePopUp.toggle();
                    } else if (stage.getKeyboardFocus() == playerName) {
                        stage.setKeyboardFocus(gameCode);
                    } else if (stage.getKeyboardFocus() == gameCode) {
                        join.toggle();
                    }
                }
                if (keycode == Input.Keys.ESCAPE && !popUpTable.isVisible()) {
                    back.toggle();
                }
                return true;
            }
        });

        // Add button functionality.
        if (parent.doesNotContainChangeListener(join.getListeners())) {
            join.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (isNameGood && isCodeGood) {
                        try {
                            String name = playerName.getText().trim();
                            parent.gameState.clientUser.setName(name);
                            parent.gameState.clientUser.setHost(false);
                            parent.createNetworkClient();
                            parent.getClient().sendTCP(new SetUsername(name));
                            parent.getClient().sendTCP(new JoinLobby(gameCode.getText().trim()));
                        } catch (IOException e) {
                            popUpTable.setVisible(true);
                            table.setVisible(false);
                        }
                    }
                }
            });
        }

        if (parent.doesNotContainChangeListener(back.getListeners())) {
            back.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    parent.changeScreen(VoShooter.Screen.MENU);
                }
            });
        }

        if (parent.doesNotContainChangeListener(closePopUp.getListeners())) {
            closePopUp.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    popUpTable.setVisible(false);
                    table.setVisible(true);
                }
            });
        }

        if (parent.doesNotContainChangeListener(playerName.getListeners())) {
            playerName.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (playerName.getText().trim().length() >= 4) {
                        nameLengthCheck.setText("Good name");
                        nameLengthCheck.setColor(0, 255, 0, 255);
                        isNameGood = true;
                    } else {
                        nameLengthCheck.setText("Too short");
                        nameLengthCheck.setColor(255, 0, 0, 255);
                        isNameGood = false;
                    }
                }
            });
        }

        if (parent.doesNotContainChangeListener(gameCode.getListeners())) {
            gameCode.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (gameCode.getText().length() < 6) {
                        gameCodeCheck.setText("Too short");
                        gameCodeCheck.setColor(255, 0, 0, 255);
                        isCodeGood = false;
                    } else if (gameCode.getText().length() > 6) {
                        gameCodeCheck.setText("Too long");
                        gameCodeCheck.setColor(255, 0, 0, 255);
                        isCodeGood = false;
                    } else if (!gameCode.getText().matches("^[a-zA-Z]*$")) {
                        gameCodeCheck.setText("Only letters A-Z");
                        gameCodeCheck.setColor(255, 0, 0, 255);
                        isCodeGood = false;
                    } else {
                        if (parent.getLobbyCode() == null || !gameCode.getText().toUpperCase().equals(parent.getLobbyCode())) {
                            gameCodeCheck.setText("Good code");
                            gameCodeCheck.setColor(0, 255, 0, 255);
                            isCodeGood = true;
                        } else {
                            gameCodeCheck.setText("No such lobby");
                            gameCodeCheck.setColor(255, 0, 0, 255);
                            isCodeGood = false;
                        }
                    }
                }
            });
        }

        if (parent.doesNotContainChangeListener(closePopUp.getListeners())) {
            closePopUp.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    popUpTable.setVisible(false);
                    table.setVisible(true);
                }
            });
        }
    }

    /**
     * Render the elements defined in the show() method.
     */
    @Override
    public void render(float delta) {
        if (shouldChangeScreen != null) {
            parent.changeScreen(shouldChangeScreen);
            shouldChangeScreen = null;
        }
        // Refresh the graphics renderer every cycle.
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (gameCode.getText().length() < 6) {
            gameCodeCheck.setText("Too short");
            gameCodeCheck.setColor(255, 0, 0, 255);
            isCodeGood = false;
        } else if (gameCode.getText().length() > 6) {
            gameCodeCheck.setText("Too long");
            gameCodeCheck.setColor(255, 0, 0, 255);
            isCodeGood = false;
        } else if (!gameCode.getText().matches("^[a-zA-Z]*$")) {
            gameCodeCheck.setText("Only letters A-Z");
            gameCodeCheck.setColor(255, 0, 0, 255);
            isCodeGood = false;
        } else {
            isCodeGood = true;
            if (parent.getDisconnectReason() != null) {
                gameCodeCheck.setText(String.valueOf(parent.getDisconnectReason()));
                gameCodeCheck.setColor(255, 0, 0, 255);
            } else if (parent.getLobbyCode() == null) {
                gameCodeCheck.setText("Good code");
                gameCodeCheck.setColor(0, 255, 0, 255);
            }
        }

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

package ee.taltech.voshooter.screens;

import java.io.IOException;

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
import ee.taltech.voshooter.networking.messages.JoinLobby;
import ee.taltech.voshooter.networking.messages.SetUsername;


public class JoinGameScreen implements Screen {

    private VoShooter parent;
    private Stage stage;
    private TextButton join;
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
        table.setFillParent(true);
        stage.addActor(table);

        join = new TextButton("Join lobby", skin);
        TextButton back = new TextButton("Back", skin);

        // Player name field and label.
        Table nameTable = new Table();
        Label enterName = new Label("Enter your username: ", skin);
        TextField playerName = new TextField("", skin);
        nameLengthCheck = new Label("Too short", skin);
        nameLengthCheck.setColor(255, 0, 0, 255);

        // Lobby code field and label.
        Table codeTable = new Table();
        Label enterCode = new Label("Enter lobby code: ", skin);
        TextField gameCode = new TextField("", skin);
        gameCodeCheck = new Label("Too short", skin);
        gameCodeCheck.setColor(255, 0, 0, 255);

        nameTable.add(enterName).left().pad(0, 0, 0, 10);
        nameTable.add(playerName).right();
        table.add(nameTable).fill();
        table.row().pad(10, 0, 0, 0);
        table.add(nameLengthCheck).fill();
        table.row().pad(10, 0, 0, 0);
        codeTable.add(enterCode).left().pad(0, 0, 0, 10);
        codeTable.add(gameCode).right();
        table.add(codeTable).fill();
        table.row().pad(10, 0, 0, 0);
        table.add(gameCodeCheck).fill();
        table.row().pad(10, 0, 0, 0);
        table.add(join).fill().maxWidth(234);
        table.row().pad(10, 0, 0, 0);
        bottomTable.add(back).left().pad(0, 0, 0, 10);
        table.add(bottomTable).fill();

        // Add button functionality.
        join.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (isNameGood && isCodeGood) {
                    try {
                        String name = playerName.getText().trim();
                        parent.gameState.clientUser.setName(name);
                        parent.createNetworkClient();
                        parent.getClient().sendTCP(new SetUsername(name));
                        parent.getClient().sendTCP(new JoinLobby(gameCode.getText().trim()));
                    } catch (IOException e) {
                       //.
                    }
                }
            }
        });

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(VoShooter.Screen.MENU);
            }
        });

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
                    gameCodeCheck.setText("Good code");
                    gameCodeCheck.setColor(0, 255, 0, 255);
                    isCodeGood = true;
                }
            }
        });
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
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    /**
     * Dispose of the screen.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}

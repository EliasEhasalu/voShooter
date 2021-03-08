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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.voshooter.VoShooter;

import java.util.ArrayList;
import java.util.List;

import static ee.taltech.voshooter.VoShooter.Screen.MAIN;

public class LobbyScreen implements Screen {

    private VoShooter parent;
    private Stage stage;
    private List<Label> playerNameLabels = new ArrayList<>();

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
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        // Add a table which will contain game creation settings.
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Create the menu objects for our stage.
        Label lobbyTitleLabel = new Label("Lobby", skin);
        TextButton leaveButton = new TextButton("Leave", skin);
        TextButton startGame = new TextButton("Start", skin);
        if (!parent.client.clientUser.isHost()) startGame.setVisible(false);

        // Add the objects to the table.
        table.add(lobbyTitleLabel);
        table.row();
        for (Label playerName : playerNameLabels) {
            table.add(playerName).left();
            table.row().pad(10, 0, 0, 0);
        }
        table.add(leaveButton);

        startGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (parent.client.clientUser.isHost()) {
                    playerNameLabels.clear();
                    parent.changeScreen(MAIN);
                }
            }
        });
    }

    /**
     * Render the elements defined in the show() method.
     */
    @Override
    public void render(float delta) {
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

package ee.taltech.voshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.voshooter.VoShooter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JoinGameScreen implements Screen {

    private VoShooter parent;
    private Stage stage;
    private List<HashMap<String, String>> gameList;

    /**
     * Construct the menu screen.
     * @param parent A reference to the orchestrator object.
     */
    public JoinGameScreen(VoShooter parent) {
        this.parent = parent;
        gameList = getGamesFromServer();

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
        Table gameTable = new Table();
        Table bottomTable = new Table();
        ScrollPane scrollPane = new ScrollPane(gameTable);
        table.setFillParent(true);
        stage.addActor(table);

        // Create the menu objects for our stage.
        List<TextButton> joinButtons = new ArrayList<>();
        List<Label> joinLabels = new ArrayList<>();
        for (HashMap<String, String> map : gameList) {
            TextButton button = new TextButton("Join", skin);
            Label label = new Label("gameName - Players: 0/8", skin);
            joinLabels.add(label);
            joinButtons.add(button);
        }
        TextButton back = new TextButton("Back", skin);
        TextButton refresh = new TextButton("Refresh", skin);

        // Add the objects to the table.
        for (int i = 0; i < joinButtons.size(); i++) {
            gameTable.row().pad(10, 0, 0, 30);
            Button button = joinButtons.get(i);
            Label label = joinLabels.get(i);
            gameTable.add(label).left();
            gameTable.add(button).right();
        }
        table.add(scrollPane).width(600f).height(300f);
        table.row().pad(10, 0, 0, 0);
        bottomTable.add(back).left();
        bottomTable.add(refresh).right();
        table.add(bottomTable).fill();

        // Add button functionality.
        refresh.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameList.clear();
                gameList = getGamesFromServer();
            }
        });

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(VoShooter.Screen.MENU);
            }
        });
        for (TextButton button : joinButtons) {
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    System.out.println("JOIN GAME");
                }
            });
        }
    }

    /**
     * Ask for a list of games from the server.
     * @return List of current games.
     */
    private List<HashMap<String, String>> getGamesFromServer() {
        List<HashMap<String, String>> gameList = new ArrayList();
        for (int i = 0; i < 20; i++) {
            HashMap<String, String> map = new HashMap<>();
            gameList.add(map);
        }
        return gameList;
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

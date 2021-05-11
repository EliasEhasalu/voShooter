package ee.taltech.voshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.voshooter.VoShooter;
import ee.taltech.voshooter.map.GameMap;
import ee.taltech.voshooter.networking.messages.clientreceived.GameEnd;

import java.util.List;

public class EndScreen implements Screen {

    private VoShooter parent;
    private Stage stage;
    private Skin skin;
    private Table table;
    private double timeElapsed = 0;

    private int gameMode;
    private double gameLength;
    private int playerCount;
    private int botAmount;
    private GameMap.MapType mapType;
    private List<String> playerStats;

    private Label playerLabel;
    private Label lengthLabel;
    private Label gameModeLabel;
    private Label botLabel;
    private Label mapTypeLabel;

    public EndScreen(VoShooter parent) {
        this.parent = parent;
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        timeElapsed = 10;
        Gdx.input.setInputProcessor(stage);
        stage.clear();
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.add(lengthLabel).row();
        table.add(gameModeLabel).row();
        table.add(mapTypeLabel).row();
        table.add(playerLabel).row();
        table.add(botLabel).row();
    }

    @Override
    public void render(float delta) {
        timeElapsed -= 1 / 60f;
        if (timeElapsed <= 0) parent.changeScreen(VoShooter.Screen.LOBBY);
        // Refresh the graphics renderer every cycle.
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        setTable();
        // And draw over it again.
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));  // Cap menu FPS to 30.
        stage.draw();
    }

    public void setTable() {
        playerLabel = new Label("Player count: " + playerCount, skin);
        lengthLabel = new Label("Game length: " + gameLength, skin);
        gameModeLabel = new Label("Gamemode: " + parent.lobbySettingsScreen.gameModes.get(gameMode), skin);
        botLabel = new Label("Bot count: " + botAmount, skin);
        mapTypeLabel = new Label("Map: " + mapType, skin);
        table.clear();
        table.add(lengthLabel).left().padBottom(10).row();
        table.add(gameModeLabel).left().padBottom(10).row();
        table.add(mapTypeLabel).left().padBottom(10).row();
        table.add(playerLabel).left().padBottom(10).row();
        table.add(botLabel).left().padBottom(80).row();
        for (String player : playerStats) {
            table.add(new Label(player, skin)).left().padBottom(10).row();
        }
        table.add(new Label("Returning to lobby in " + Math.round(timeElapsed * 10f) / 10f, skin)).left().padTop(60);
    }

    public void setStatistics(GameEnd msg) {
        gameMode = msg.gameMode;
        gameLength = msg.gameLength;
        playerCount = msg.playerCount;
        botAmount = msg.botAmount;
        mapType = msg.mapType;
        playerStats = msg.leaderBoard;
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

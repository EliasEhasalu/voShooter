package ee.taltech.voshooter.gamestate.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.voshooter.entity.player.ClientPlayer;
import ee.taltech.voshooter.rendering.Drawable;
import ee.taltech.voshooter.screens.MainScreen;

import java.util.Comparator;
import java.util.stream.Collectors;

import static ee.taltech.voshooter.screens.MainScreen.STATS_ROW_PAD;

public class ClientGameModeManager {

    protected MainScreen mainScreen;
    protected SpriteBatch hudBatch;
    protected BitmapFont font = new BitmapFont(Gdx.files.internal("bitmapFont/commodore.fnt"),
                Gdx.files.internal("bitmapFont/commodore.png"), false);
    protected double timePassed = 0;

    public ClientGameModeManager(MainScreen screen, SpriteBatch batch) {
        this.mainScreen = screen;
        this.hudBatch = batch;
    }

    public void setTimePassed(double timePassed) {
        this.timePassed = timePassed;
    }

    public void update() {
        if (mainScreen.statsTabOpen) drawStatistics();
    }

    public void drawStatistics() {
        hudBatch.begin();
        int tableTop = Gdx.graphics.getHeight() - 30;
        int tableLeft = Gdx.graphics.getWidth() / 2 - 2 * STATS_ROW_PAD;
        int gameLength = mainScreen.parent.gameState.currentLobby.getGameLength();
        if (gameLength >= 15) font.draw(hudBatch, String.format("Game length: %s",
                mainScreen.parent.gameState.currentLobby.getGameLength()), tableLeft, tableTop);
        else font.draw(hudBatch, "Endless game", tableLeft, tableTop);
        font.draw(hudBatch, String.format("Time elapsed: %s", Math.round(timePassed)),
                tableLeft + (STATS_ROW_PAD * 3), tableTop);
        tableTop -= 20;
        font.draw(hudBatch, "Player names", tableLeft, tableTop);
        font.draw(hudBatch, "Kills", tableLeft + (STATS_ROW_PAD * 2), tableTop);
        font.draw(hudBatch, "Deaths", tableLeft + (STATS_ROW_PAD * 3), tableTop);
        font.draw(hudBatch, "KDR", tableLeft + (STATS_ROW_PAD * 4), tableTop);
        tableTop -= 20;
        for (ClientPlayer player : mainScreen.parent.gameState.getPlayers().values().stream()
                .sorted(Comparator.comparing(Drawable::getKills).reversed()).collect(Collectors.toList())) {
            if (player.isBot()) font.setColor(Color.CYAN);
            else font.setColor(Color.WHITE);
            font.draw(hudBatch, player.getName(), tableLeft, tableTop);
            font.setColor(Color.WHITE);
            font.draw(hudBatch, String.valueOf(player.getKills()), tableLeft + (STATS_ROW_PAD * 2), tableTop);
            font.draw(hudBatch, String.valueOf(player.getDeaths()), tableLeft + (STATS_ROW_PAD * 3), tableTop);
            if (player.getDeaths() > 0) {
                font.draw(hudBatch,
                        String.valueOf((double) Math.round((player.getKills()
                                / (double) player.getDeaths()) * 100) / 100),
                        tableLeft + (STATS_ROW_PAD * 4), tableTop);
            } else {
                font.draw(hudBatch, String.valueOf(player.getKills()), tableLeft + (STATS_ROW_PAD * 4), tableTop);
            }
            tableTop -= 20;
        }
        font.getData().setScale(1f);
        font.draw(hudBatch, mainScreen.parent.gameState.currentLobby.getLobbyCode(),
                Gdx.graphics.getWidth() / (float) 2 - 80, 50);
        font.getData().setScale(0.6f);
        hudBatch.end();
    }
}

package ee.taltech.voshooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.voshooter.AppPreferences;
import ee.taltech.voshooter.VoShooter;
import ee.taltech.voshooter.controller.ActionType;
import ee.taltech.voshooter.controller.GameController;
import ee.taltech.voshooter.entity.clientprojectile.ClientProjectile;
import ee.taltech.voshooter.entity.player.ClientPlayer;
import ee.taltech.voshooter.gamestate.ChatEntry;
import ee.taltech.voshooter.gamestate.DeathMessage;
import ee.taltech.voshooter.gamestate.gamemode.ClientGameModeManager;
import ee.taltech.voshooter.gamestate.gamemode.ManagerBuilder;
import ee.taltech.voshooter.map.GameMap;
import ee.taltech.voshooter.networking.messages.serverreceived.ChangeWeapon;
import ee.taltech.voshooter.networking.messages.serverreceived.ChatSendMessage;
import ee.taltech.voshooter.networking.messages.serverreceived.LeaveLobby;
import ee.taltech.voshooter.networking.messages.serverreceived.MouseCoords;
import ee.taltech.voshooter.networking.messages.serverreceived.MovePlayer;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerAction;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerDash;
import ee.taltech.voshooter.networking.messages.serverreceived.PlayerInput;
import ee.taltech.voshooter.networking.messages.serverreceived.Shoot;
import ee.taltech.voshooter.rendering.Drawable;
import ee.taltech.voshooter.soundeffects.MusicPlayer;
import ee.taltech.voshooter.soundeffects.SoundPlayer;
import ee.taltech.voshooter.weapon.Weapon;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;


public class MainScreen implements Screen {

    public static final float MINIMAP_ZOOM = 20f;
    public static final int STATS_ROW_PAD = 120;
    public final VoShooter parent;
    private final Stage stage;
    private final Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
    public VoShooter.Screen shouldChangeScreen;
    private BitmapFont font;
    private BitmapFont killfeedFont;
    private boolean pauseMenuActive;
    private final TextButton exitButton = new TextButton("Exit", skin);
    private TextButton resumeButton;
    private final TextButton settingsButton = new TextButton("Settings", skin);
    private final TextField chatTextField = new TextField("", skin);
    private final OrthographicCamera camera = new OrthographicCamera();
    private OrthographicCamera minimapCamera;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private TiledMapRenderer miniMapRenderer;
    private final SpriteBatch minimapBatch = new SpriteBatch();
    private final Texture minimapPlayer = new Texture("textures/playerIcon.png");
    public static final float MINIMAP_RATIO = 16f / 9f;
    public static final int MINIMAP_HEIGHT = 100;
    public static final int MINIMAP_WIDTH = (int) (MINIMAP_HEIGHT * MINIMAP_RATIO);
    public static final int MINIMAP_MARGIN = 50;
    public static final int MARKER_SIZE = 20;
    public static final float MINIMAP_SCALE = 0.22f;

    public static final Map<Weapon.Type, Sprite> WEAPON_SPRITES = new HashMap<Weapon.Type, Sprite>() {{
        put(Weapon.Type.PISTOL,             new Sprite(new Texture("textures/player/playerpistol.png")));
        put(Weapon.Type.SHOTGUN,            new Sprite(new Texture("textures/player/playershotgun.png")));
        put(Weapon.Type.ROCKET_LAUNCHER,    new Sprite(new Texture("textures/player/playerrocketlauncher.png")));
        put(Weapon.Type.FLAMETHROWER,       new Sprite(new Texture("textures/player/playerflamethrower.png")));
        put(Weapon.Type.MACHINE_GUN,        new Sprite(new Texture("textures/player/playermachinegun.png")));
    }};

    private final SpriteBatch hudBatch = new SpriteBatch();

    public static final Map<Weapon.Type, Texture> WEAPON_TEXTURES = new HashMap<Weapon.Type, Texture>() {{
        put(Weapon.Type.PISTOL,             new Texture("textures/hud/item/handgun.png"));
        put(Weapon.Type.SHOTGUN,            new Texture("textures/hud/item/shotgun.png"));
        put(Weapon.Type.ROCKET_LAUNCHER,    new Texture("textures/hud/item/rocketlauncher.png"));
        put(Weapon.Type.FLAMETHROWER,       new Texture("textures/hud/item/flamethrower.png"));
        put(Weapon.Type.MACHINE_GUN,        new Texture("textures/hud/item/machinegun.png"));
    }};
    private final Texture selectedGunBackground
                                        = new Texture("textures/hud/background/selectedGunBackground.png");
    private final Texture healthEmpty   = new Texture("textures/hud/background/healthBarEmpty.png");
    private final Texture healthFull    = new Texture("textures/hud/background/healthBarFull.png");
    private final Texture killIcon      = new Texture("textures/hud/background/killicon.png");
    private final Texture selfKillIcon  = new Texture("textures/hud/background/selfkillicon.png");

    private float healthFraction = 1.00f;
    private int currentAmmo = 16;
    private int maxAmmo = 20;
    public boolean statsTabOpen = false;
    public boolean chatActive = false;
    public boolean chatOpen = false;
    public ClientGameModeManager clientGameModeManager;
    public static final int KILLFEED_TOP_MARGIN = 50;
    private static final int KILLFEED_RIGHT_MARGIN = 50;
    private static final int KILLFEED_GAP = 38;
    private static final int KILLFEED_ICON_SPACE = 10;

    /**
     * Construct the menu screen.
     * @param parent A reference to the orchestrator object.
     */
    public MainScreen(VoShooter parent) {
        this.parent = parent;

        // Create stage which will contain this screen's objects
        stage = new Stage(new ScreenViewport());
    }

    /**
     * Define the assets to be shown in the render loop.
     */
    @Override
    public void show() {
        clientGameModeManager = ManagerBuilder.getGameModeManager(parent.gameState.currentLobby.getGamemode(), camera,
                this, hudBatch);
        if (this.parent.gameState.userPlayer != null) {
            this.parent.gameState.userPlayer.clientGameModeManager = this.clientGameModeManager;
        }
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        camera.setToOrtho(false, width, height);
        camera.zoom = 0.8f;
        camera.update();
        tiledMap = new TmxMapLoader().load(GameMap.getTileSet(parent.gameState.currentLobby.getMap()));
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        font = new BitmapFont(Gdx.files.internal("bitmapFont/commodore.fnt"),
                Gdx.files.internal("bitmapFont/commodore.png"), false);
        font.getData().setScale(0.6f);
        killfeedFont = new BitmapFont(Gdx.files.internal("bitmapFont/pixeloperator.fnt"),
                Gdx.files.internal("bitmapFont/pixeloperator.png"), false);
        MusicPlayer.stopMusic();

        // Have it handle player's input.
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        miniMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, MINIMAP_SCALE);
        minimapCamera = new OrthographicCamera(MINIMAP_WIDTH, MINIMAP_HEIGHT);
        minimapCamera.zoom = MINIMAP_ZOOM;
        minimapCamera.setToOrtho(false, Gdx.graphics.getWidth() / 10f,
                Gdx.graphics.getHeight() / 10f);
        minimapCamera.update();
        createMenuButtons();
    }

    private void setClientGameModeManager() {
        if (this.parent.gameState.userPlayer.clientGameModeManager == null) {
            this.parent.gameState.userPlayer.clientGameModeManager = this.clientGameModeManager;
        }
    }

    /**
     * Render the elements defined in the show() method.
     */
    @Override
    public void render(float delta) {
        // Refresh the graphics renderer every cycle.
        Gdx.gl.glClearColor(0.0862745f, 0.0862745f, 0.0862745f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!pauseMenuActive && !chatOpen) {
            // Send player inputs to server every render loop.
            handlePlayerInputs();
        }
        moveCameraToPlayer();
        setClientGameModeManager();
        camera.update();
        minimapCamera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        healthFraction = parent.gameState.userPlayer.getHealth() / 100f;

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 64f));  // Cap FPS to 64.
        stage.draw();

        stage.getBatch().setProjectionMatrix(camera.combined);
        stage.getBatch().begin();
        drawEntities();
        drawProjectiles();
        drawParticles();
        drawRespawnTimer();
        stage.getBatch().end();
        clientGameModeManager.update();

        drawMiniMap();
        drawHUD();
    }

    /**
     * Send player inputs to server.
     * Might also perform other things based on inputs client-side in the future.
     */
    private void handlePlayerInputs() {
        List<ActionType> inputs = GameController.getInputs();
        Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        List<PlayerAction> inputsToSend = new ArrayList<>();
        ClientPlayer p = parent.gameState.userPlayer;
        inputsToSend.add(new MouseCoords(mousePos.x - p.getPosition().x, mousePos.y - p.getPosition().y));

        for (ActionType a : inputs) {
            switch (a) {
                case MOVE_LEFT:
                    inputsToSend.add(new MovePlayer(-1, 0));
                    break;
                case MOVE_RIGHT:
                    inputsToSend.add(new MovePlayer(1, 0));
                    break;
                case MOVE_UP:
                    inputsToSend.add(new MovePlayer(0, 1));
                    break;
                case MOVE_DOWN:
                    inputsToSend.add(new MovePlayer(0, -1));
                    break;
                case MOUSE_LEFT:
                    inputsToSend.add(new Shoot());
                    break;
                case WEAPON_PISTOL:
                    inputsToSend.add(new ChangeWeapon(ActionType.WEAPON_PISTOL));
                    break;
                case WEAPON_SHOTGUN:
                    inputsToSend.add(new ChangeWeapon(ActionType.WEAPON_SHOTGUN));
                    break;
                case WEAPON_RPG:
                    inputsToSend.add(new ChangeWeapon(ActionType.WEAPON_RPG));
                    break;
                case WEAPON_FLAMETHROWER:
                    inputsToSend.add(new ChangeWeapon(ActionType.WEAPON_FLAMETHROWER));
                    break;
                case WEAPON_MACHINE_GUN:
                    inputsToSend.add(new ChangeWeapon(ActionType.WEAPON_MACHINE_GUN));
                    break;
                case DASH:
                    inputsToSend.add(new PlayerDash());
                    break;
                default:
                    break;
            }
        }

        // Send all inputs this frame to server.
        parent.getClient().sendTCP(new PlayerInput(inputsToSend));
    }

    /**
     * Set the camera position to the players position.
     */
    private void moveCameraToPlayer() {
        final float maxCameraDist = 150;
        final float minCameraTranslate = 0.3f;
        final Vector2 playerPos = parent.gameState.userPlayer.getPosition();
        final Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        Vector2 vecToPanPoint = new Vector2(mousePos.x - playerPos.x, mousePos.y - playerPos.y);
        vecToPanPoint.limit(maxCameraDist);
        final Vector2 vecFromCamera;
        if (chatOpen || pauseMenuActive) {
            vecFromCamera = new Vector2(playerPos.x - camera.position.x, playerPos.y - camera.position.y);
        } else {
            vecFromCamera = new Vector2(playerPos.x + vecToPanPoint.x - camera.position.x,
                    playerPos.y + vecToPanPoint.y - camera.position.y);
        }

        float xTranslate = vecFromCamera.x / 15;
        float yTranslate = vecFromCamera.y / 15;

        camera.translate(xTranslate, yTranslate);
    }

    /**
     * Create the buttons for the menu and add functionality to them.
     */
    private void createMenuButtons() {
        // Add a table which will contain menu items to the stage.
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        stage.addActor(chatTextField);
        chatTextField.setVisible(false);
        chatTextField.setMaxLength(60);

        // Create the objects in the scene.
        resumeButton = new TextButton("Resume", skin);
        if (!pauseMenuActive) {
            setPauseTableVisibility(false);
        }

        // Add the buttons to the table.
        table.add(resumeButton).fillX();
        table.row().padTop(10);
        table.add(settingsButton).fillX();
        table.row().padTop(10);
        table.add(exitButton).fillX();

        // Add button functionality.
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    setPauseTableVisibility(!resumeButton.isVisible());
                } else if (keycode == Input.Keys.TAB) {
                    statsTabOpen = true;
                } else if (keycode == Input.Keys.ENTER) {
                    chatOpen = !chatOpen;
                    handleChatInput();
                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.TAB) {
                    statsTabOpen = false;
                }
                return true;
            }
        });

        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setPauseTableVisibility(false);
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.setCameFromGame(true);
                parent.screen = null;
                parent.changeScreen(VoShooter.Screen.PREFERENCES);
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.gameState.clearDrawables();
                pauseMenuActive = false;
                parent.screen = null;
                parent.getClient().sendTCP(new LeaveLobby());
                parent.changeScreen(VoShooter.Screen.MENU);
            }
        });
    }

    /**
     * Set the pause table visibility.
     * @param visibility to set to
     */
    private void setPauseTableVisibility(boolean visibility) {
        resumeButton.setVisible(visibility);
        settingsButton.setVisible(visibility);
        exitButton.setVisible(visibility);
        pauseMenuActive = visibility;
    }

    /**
     * Draw all drawable entities to the screen.
     */
    private void drawEntities() {
        for (Drawable drawable : parent.gameState.getDrawables()) {
            if (drawable.isVisible()) {
                drawable.getSprite().draw(stage.getBatch());
            }
            if (drawable instanceof ClientPlayer) {
                final Sprite sprite = WEAPON_SPRITES.get(((ClientPlayer) drawable).getHeldWeapon());
                sprite.setPosition(drawable.getSprite().getX(), drawable.getSprite().getY());
                sprite.setRotation(drawable.getSprite().getRotation());
                sprite.setScale(drawable.getSprite().getScaleX(), drawable.getSprite().getScaleY());
                sprite.draw(stage.getBatch());

                font.draw(stage.getBatch(), ((ClientPlayer) drawable).getName(),
                        drawable.getPosition().x - (((ClientPlayer) drawable).getName().length() * 7),
                        drawable.getPosition().y + 40);
            }
        }
    }

    /**
     * Draw the projectiles to the screen.
     */
    private void drawProjectiles() {
        for (ClientProjectile p : parent.gameState.getProjectiles().values()) {
            p.getSprite().setRotation(p.getVelocity().angleDeg());
            p.getSprite().draw(stage.getBatch());
        }
    }

    /**
     * Draw the respawn timer.
     */
    private void drawRespawnTimer() {
        ClientPlayer player = parent.gameState.userPlayer;
        if (player.getHealth() <= 0) {
            font.draw(stage.getBatch(),
                    String.format("Respawning in %s seconds", (double) Math.round(player.respawnTimer * 10) / 10),
                    player.getPosition().x, player.getPosition().y);
        }
    }

    /**
     * Draw the HUD in the render cycle.
     */
    private void drawHUD() {
        final int width = Gdx.graphics.getWidth();
        final int height = Gdx.graphics.getHeight();

        hudBatch.begin();

        // Draw UI particles.
        if (AppPreferences.getParticlesOn()) {
            for (ParticleEffect pe : parent.gameState.getUiParticles()) {
                pe.update(Gdx.graphics.getDeltaTime());
                pe.draw(hudBatch);
                if (pe.isComplete()) {
                    parent.gameState.particleEffectFinished(pe);
                }
            }
        }

        // Draw killfeed
        killfeedFont.setColor(Color.WHITE);
        int i = parent.gameState.deathMessages.size() - 1;
        Queue<DeathMessage> messages = new ArrayDeque<>(parent.gameState.deathMessages);
        for (DeathMessage msg : messages) {
            GlyphLayout playerLayout = new GlyphLayout();
            playerLayout.setText(killfeedFont, msg.getPlayer().getName());

            final int playerX = width - (int) playerLayout.width - KILLFEED_RIGHT_MARGIN;
            final int playerY = height - (i * KILLFEED_GAP) - KILLFEED_TOP_MARGIN;

            if (msg.getKiller() != msg.getPlayer()) {
                GlyphLayout killerLayout = new GlyphLayout();
                killerLayout.setText(killfeedFont, msg.getKiller().getName());

                hudBatch.draw(killIcon,
                        playerX - killIcon.getWidth() - KILLFEED_ICON_SPACE,
                        playerY - killIcon.getHeight() / 1.4f);
                killfeedFont.draw(hudBatch, killerLayout,
                        playerX - killIcon.getWidth() - killerLayout.width - 2 * KILLFEED_ICON_SPACE,
                        playerY);
            } else {
                hudBatch.draw(selfKillIcon,
                        playerX - selfKillIcon.getWidth() - KILLFEED_ICON_SPACE,
                        playerY - selfKillIcon.getHeight() / 1.4f);
            }

            killfeedFont.draw(hudBatch, playerLayout, playerX, playerY);

            if (msg.tick()) {
                parent.gameState.removeDeathMessage();
            }
            i--;
        }

        hudBatch.draw(selectedGunBackground, 64, 32, 96, 96);
        hudBatch.draw(WEAPON_TEXTURES.getOrDefault(parent.gameState.userPlayer.getWeapon(),
                WEAPON_TEXTURES.get(Weapon.Type.PISTOL)), 64, 32, 96, 96);

        hudBatch.draw(healthEmpty, width / 2f - (healthEmpty.getWidth() / 2f), 88);
        hudBatch.draw(healthFull, width / 2f - (healthFull.getWidth() / 2f), 88, 0, 0,
                Math.round(healthFull.getWidth() * Math.max(healthFraction, 0)), healthFull.getHeight());

        // Draw ammo
        final float fontScaleX = font.getScaleX();
        final float fontScaleY = font.getScaleY();
        final int ammoX = 164;
        final int ammoY = 80;

        font.getData().setScale(0.8f);
        font.setColor(Color.WHITE);
        GlyphLayout currentAmmoStr = new GlyphLayout(font, String.valueOf(parent.gameState.userPlayer.currentAmmo));
        if (parent.gameState.userPlayer.currentAmmo == Integer.MAX_VALUE) {
            currentAmmoStr.setText(font, "INF");
        }
        font.draw(hudBatch, currentAmmoStr, ammoX, ammoY);

//        font.getData().setScale(0.6f);
//        font.setColor(Color.LIGHT_GRAY);
//        GlyphLayout maxAmmoStr = new GlyphLayout(font, "/" + parent.gameState.userPlayer.maxAmmo);
//        font.draw(hudBatch, maxAmmoStr, ammoX + currentAmmoStr.width + 4, ammoY - maxAmmoStr.height / 1.5f);

        font.getData().setScale(fontScaleX, fontScaleY);
        font.setColor(Color.WHITE);

        drawChat();

        hudBatch.end();
    }

    private void drawChat() {
        final int width = Gdx.graphics.getWidth();
        final int height = Gdx.graphics.getHeight();
        final int chatX = 16;
        final int chatY = 300;
        final int chatGap = 20;
        BitmapFont chatFont = killfeedFont;
        chatFont.setColor(0, 1, 1, 1);

        int i = parent.gameState.chatEntries.size() - 1;
        Queue<ChatEntry> messages = new ArrayDeque<>(parent.gameState.chatEntries);

        for (ChatEntry entry : messages) {
            final float opacity;
            if (chatActive) opacity = 1;
            else opacity = Math.min(Math.max(
                    entry.getDuration() / (ChatEntry.MAX_DURATION / 10f), 0f), 1f);
            if (entry.getPrefix().equals("[Server]: ")) chatFont.setColor(Color.LIGHT_GRAY.r, Color.LIGHT_GRAY.g,
                    Color.LIGHT_GRAY.b, opacity);
            else chatFont.setColor(0, 1, 1, opacity);

            GlyphLayout wholeMessage = new GlyphLayout();
            wholeMessage.setText(chatFont, entry.getPrefix() + entry.getText());

            chatFont.draw(hudBatch, wholeMessage, chatX, chatY + i * chatGap);

            entry.tick();
//            if (entry.tick()) parent.gameState.removeChatEntry();
            i--;
        }

        if (chatActive) {
            chatFont.setColor(0, 1, 1, 1);

            String inputString = "[Type]: " + chatTextField.getText();
            GlyphLayout inputRow = new GlyphLayout();
            inputRow.setText(chatFont, inputString);
            chatFont.draw(hudBatch, inputRow, chatX, chatY - chatX);
        }
    }

    /**
     * Draw the minimap in the render cycle.
     */
    private void drawMiniMap() {
        if (AppPreferences.getMinimapOn()) {
            miniMapRenderer.setView(minimapCamera);
            miniMapRenderer.render();
            minimapBatch.setProjectionMatrix(minimapCamera.combined);

            minimapBatch.begin();
            ClientPlayer player = parent.gameState.userPlayer;
            Vector2 pos = player.getPosition();
            minimapBatch.draw(minimapPlayer,
                    pos.x * MINIMAP_SCALE - MARKER_SIZE / 2f, pos.y * MINIMAP_SCALE - MARKER_SIZE / 2f,
                    MARKER_SIZE, MARKER_SIZE);
            minimapBatch.end();
        }
    }

    /**
     * Draw the particles to the sprite batch.
     */
    private void drawParticles() {
        if (AppPreferences.getParticlesOn()) {
            for (ParticleEffect pe : parent.gameState.getParticleEffects()) {
                pe.update(Gdx.graphics.getDeltaTime());
                pe.draw(stage.getBatch());
                if (pe.isComplete()) {
                    parent.gameState.particleEffectFinished(pe);
                }
            }
        }
    }

    /**
     * Handle chat input. Method called when ENTER key is pressed.
     */
    private void handleChatInput() {
        if (!pauseMenuActive) {
            if (chatActive) {
                if (!chatTextField.getText().equals("")) {
                    ChatSendMessage msg = new ChatSendMessage(chatTextField.getText());
                    parent.getClient().sendTCP(msg);
                }
                stage.setKeyboardFocus(null);
            } else {
                stage.setKeyboardFocus(chatTextField);
            }
            chatTextField.setText("");
            chatActive = !chatActive;
        }
    }

    /**
     * Make sure the window doesn't break.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        hudBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        camera.setToOrtho(false, width, height);

        minimapCamera.setToOrtho(false, width / 10f, height / 10f);
        minimapCamera.position.x = width - MINIMAP_MARGIN;
        minimapCamera.position.y = -height + (450) + MINIMAP_MARGIN;
        minimapCamera.update();
    }

    /**
     * Placeholder.
     */
    @Override
    public void pause() {
    }

    /**
     * Placeholder.
     */
    @Override
    public void resume() {
    }

    /**
     * Placeholder.
     */
    @Override
    public void hide() {
    }

    /**
     * Dispose of the screen.
     */
    @Override
    public void dispose() {
        SoundPlayer.dispose();
        stage.dispose();
    }
}

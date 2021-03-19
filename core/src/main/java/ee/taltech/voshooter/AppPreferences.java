package ee.taltech.voshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;

public class AppPreferences {

    private static final String PREF_MUSIC_VOLUME = "volume";
    private static final String PREF_SOUND_VOL = "sound";
    private static final String PREFS_NAME = "voshooter";
    private static final String PREF_PLAYER_COUNT = "4";
    private static final String PREF_GAME_MODE = "gamemode";
    private static final String PREF_MOVE_UP_KEY = "up key";
    private static final String PREF_MOVE_DOWN_KEY = "down key";
    private static final String PREF_MOVE_LEFT_KEY = "left key";
    private static final String PREF_MOVE_RIGHT_KEY = "right key";
    private static final String PREF_MOUSE_LEFT = "mouse left";
    private static final String PREF_MOUSE_RIGHT = "mouse right";

    /** @return A preferences object containing the player's preferences */
    protected static Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    /**
     * @return The desired music volume.
     */
    public static float getMusicVolume() {
        return getPrefs().getFloat(PREF_MUSIC_VOLUME, 0.5f);
    }

    /**
     * @param volume The desired music volume.
     */
    public static void setMusicVolume(float volume) {
        getPrefs().putFloat(PREF_MUSIC_VOLUME, volume);
        // Write to disk.
        getPrefs().flush();
    }

    /**
     * @return The sound volume.
     */
    public static float getSoundVolume() {
        return getPrefs().getFloat(PREF_SOUND_VOL, 0.5f);
    }

    /**
     * @param volume The desired sound volume.
     */
    public static void setSoundVolume(float volume) {
        getPrefs().putFloat(PREF_SOUND_VOL, volume);
        // Write to disk.
        getPrefs().flush();
    }

    /**
     * @param players the desired player amount
     */
    public static void setPlayerCount(float players) {
        getPrefs().putFloat(PREF_PLAYER_COUNT, players);
        // Write to disk.
        getPrefs().flush();
    }

    /**
     * @return the player count
     */
    public static float getPlayerCount() {
        return getPrefs().getFloat(PREF_PLAYER_COUNT, 4);
    }

    /**
     * @param gameMode the desired gamemode
     */
    public static void setGameMode(float gameMode) {
        getPrefs().putFloat(PREF_GAME_MODE, gameMode);
        // Write to disk.
        getPrefs().flush();
    }

    /**
     * @return the game mode
     */
    public static float getGameMode() {
        return getPrefs().getFloat(PREF_GAME_MODE, 1);
    }

    /** @param key Set the key that triggers MOVE_UP action. */
    public static void setUpKey(int key) {
        getPrefs().putInteger(PREF_MOVE_UP_KEY, key);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param key Set the key that triggers MOVE_DOWN action. */
    public static void setDownKey(int key) {
        getPrefs().putInteger(PREF_MOVE_DOWN_KEY, key);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param key Set the key that triggers MOVE_LEFT action. */
    public static void setLeftKey(int key) {
        getPrefs().putInteger(PREF_MOVE_LEFT_KEY, key);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param key Set the key that triggers MOVE_RIGHT action. */
    public static void setRightKey(int key) {
        getPrefs().putInteger(PREF_MOVE_RIGHT_KEY, key);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param key Set the key that triggers MOUSE_LEFT action. */
    public static void setMouseLeft(int key) {
        getPrefs().putInteger(PREF_MOUSE_LEFT, key);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param key Set the key that triggers MOUSE_RIGHT action. */
    public static void setMouseRight(int key) {
        getPrefs().putInteger(PREF_MOUSE_RIGHT, key);
        // Write to disk.
        getPrefs().flush();
    }

    /** @return Key that triggers MOVE_UP action. */
    public static int getUpKey() {
        return getPrefs().getInteger(PREF_MOVE_UP_KEY, Input.Keys.W);
    }

    /** @return Key that triggers MOVE_DOWN action. */
    public static int getDownKey() {
        return getPrefs().getInteger(PREF_MOVE_DOWN_KEY, Input.Keys.S);
    }

    /** @return Key that triggers MOVE_LEFT action. */
    public static int getLeftKey() {
        return getPrefs().getInteger(PREF_MOVE_LEFT_KEY, Input.Keys.A);
    }

    /** @return Key that triggers MOVE_RIGHT action. */
    public static int getRightKey() {
        return getPrefs().getInteger(PREF_MOVE_RIGHT_KEY, Input.Keys.D);
    }

    /** @return Key that triggers MOUSE_LEFT action. */
    public static int getMouseLeft() {
        return getPrefs().getInteger(PREF_MOUSE_LEFT, Input.Buttons.LEFT);
    }

    /** @return Key that triggers MOUSE_RIGHT action. */
    public static int getMouseRight() {
        return getPrefs().getInteger(PREF_MOUSE_RIGHT, Input.Buttons.RIGHT);
    }
}

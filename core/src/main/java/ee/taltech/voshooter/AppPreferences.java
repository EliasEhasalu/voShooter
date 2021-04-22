package ee.taltech.voshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;

import java.util.Arrays;
import java.util.List;

public class AppPreferences {

    private static final String PREFS_NAME = "voshooter";
    private static final String PREF_MUSIC_VOLUME = "volume";
    private static final String PREF_SOUND_VOL = "sound";
    private static final String PREF_PARTICLE_TOGGLE = "particles";
    private static final String PREF_MINIMAP = "minimap";
    private static final String PREF_PLAYER_COUNT = "4";
    private static final String PREF_GAME_MODE = "gamemode";
    private static final String PREF_MOVE_UP_KEY = "up key";
    private static final String PREF_MOVE_DOWN_KEY = "down key";
    private static final String PREF_MOVE_LEFT_KEY = "left key";
    private static final String PREF_MOVE_RIGHT_KEY = "right key";
    private static final String PREF_MOUSE_LEFT = "mouse left";
    private static final String PREF_MOUSE_RIGHT = "mouse right";
    private static final String PREF_UP_KEY_IS_KEY = "up key is key";
    private static final String PREF_DOWN_IS_KEY = "down key is key";
    private static final String PREF_LEFT_IS_KEY = "left key is key";
    private static final String PREF_RIGHT_IS_KEY = "right key is key";
    private static final String PREF_L_SHIFT_KEY = "left shift key";
    private static final String PREF_L_SHIFT_KEY_IS_KEY = "left shift key is key";
    private static final String PREF_MOUSE_LEFT_IS_KEY = "left button is key";
    private static final String PREF_MOUSE_RIGHT_IS_KEY = "right button is key";
    private static final String PREF_NUMBER_1 = "number 1";
    private static final String PREF_NUMBER_2 = "number 2";
    private static final String PREF_NUMBER_3 = "number 3";
    private static final String PREF_NUMBER_4 = "number 4";
    private static final String PREF_NUMBER_5 = "number 5";
    private static final String PREF_NUMBER_6 = "number 6";
    private static final String PREF_1_IS_KEY = "number 1 is key";
    private static final String PREF_2_IS_KEY = "number 2 is key";
    private static final String PREF_3_IS_KEY = "number 3 is key";
    private static final String PREF_4_IS_KEY = "number 4 is key";
    private static final String PREF_5_IS_KEY = "number 5 is key";
    private static final String PREF_6_IS_KEY = "number 6 is key";
    private static final List<String> BUTTONS_LIST = Arrays.asList("Left Button", "Right Button", "Middle Button", "Back", "Forward");

    /** @return A preferences object containing the player's preferences */
    protected static Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    /**
     * @return The desired music volume.
     */
    public static float getMusicVolume() {
        return getPrefs().getFloat(PREF_MUSIC_VOLUME, 0.2f);
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
        return getPrefs().getFloat(PREF_SOUND_VOL, 0.2f);
    }

    /**
     * @param volume The desired sound volume.
     */
    public static void setSoundVolume(float volume) {
        getPrefs().putFloat(PREF_SOUND_VOL, volume);
        // Write to disk.
        getPrefs().flush();
    }

    /** @return If particles are toggled on or off. */
    public static boolean getParticlesOn() {
        return getPrefs().getBoolean(PREF_PARTICLE_TOGGLE, true);
    }

    /**
     * Set particle toggle on or off.
     * @param on If particles should be on.
     */
    public static void setParticlesOn(boolean on) {
        getPrefs().putBoolean(PREF_PARTICLE_TOGGLE, on);
        getPrefs().flush();
    }

    /**
     * @return If the minimap is toggled on or off.
     */
    public static boolean getMinimapOn() {
        return getPrefs().getBoolean(PREF_MINIMAP, true);
    }

    /**
     * Set if the minimap is toggled on or off.
     * @param on If the minimap should be on.
     */
    public static void setMinimapOn(boolean on) {
        getPrefs().putBoolean(PREF_MINIMAP, on);
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

    /** @param isKey Set whether MOVE_UP action is key. */
    public static void setUpKeyIsKey(boolean isKey) {
        getPrefs().putBoolean(PREF_UP_KEY_IS_KEY, isKey);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param key Set the key that triggers MOVE_DOWN action. */
    public static void setDownKey(int key) {
        getPrefs().putInteger(PREF_MOVE_DOWN_KEY, key);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param isKey Set whether MOVE_DOWN action is key. */
    public static void setDownKeyIsKey(boolean isKey) {
        getPrefs().putBoolean(PREF_DOWN_IS_KEY, isKey);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param key Set the key that triggers MOVE_LEFT action. */
    public static void setLeftKey(int key) {
        getPrefs().putInteger(PREF_MOVE_LEFT_KEY, key);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param isKey Set whether MOVE_LEFT action is key. */
    public static void setLeftKeyIsKey(boolean isKey) {
        getPrefs().putBoolean(PREF_LEFT_IS_KEY, isKey);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param key Set the key that triggers MOVE_RIGHT action. */
    public static void setRightKey(int key) {
        getPrefs().putInteger(PREF_MOVE_RIGHT_KEY, key);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param isKey Set whether MOVE_RIGHT action is key. */
    public static void setRightKeyIsKey(boolean isKey) {
        getPrefs().putBoolean(PREF_RIGHT_IS_KEY, isKey);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param key Set the key that triggers MOUSE_LEFT action. */
    public static void setMouseLeft(int key) {
        getPrefs().putInteger(PREF_MOUSE_LEFT, key);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param isKey Set whether MOUSE_LEFT action is key. */
    public static void setButtonLeftIsKey(boolean isKey) {
        getPrefs().putBoolean(PREF_MOUSE_LEFT_IS_KEY, isKey);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param key Set the key that triggers MOUSE_RIGHT action. */
    public static void setMouseRight(int key) {
        getPrefs().putInteger(PREF_MOUSE_RIGHT, key);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param isKey Set whether MOUSE_RIGHT action is key. */
    public static void setButtonRightIsKey(boolean isKey) {
        getPrefs().putBoolean(PREF_MOUSE_RIGHT_IS_KEY, isKey);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param key Set the key that triggers PREF_NUMBER_1 action. */
    public static void setNumberOne(int key) {
        getPrefs().putInteger(PREF_NUMBER_1, key);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param isKey Set whether PREF_NUMBER_1 action is key. */
    public static void setNumberOneIsKey(boolean isKey) {
        getPrefs().putBoolean(PREF_1_IS_KEY, isKey);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param key Set the key that triggers PREF_NUMBER_2 action. */
    public static void setNumberTwo(int key) {
        getPrefs().putInteger(PREF_NUMBER_2, key);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param isKey Set whether PREF_NUMBER_2 action is key. */
    public static void setNumberTwoIsKey(boolean isKey) {
        getPrefs().putBoolean(PREF_2_IS_KEY, isKey);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param key Set the key that triggers PREF_NUMBER_3 action. */
    public static void setNumberThree(int key) {
        getPrefs().putInteger(PREF_NUMBER_3, key);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param isKey Set whether PREF_NUMBER_3 action is key. */
    public static void setNumberThreeIsKey(boolean isKey) {
        getPrefs().putBoolean(PREF_3_IS_KEY, isKey);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param key Set the key that triggers PREF_NUMBER_4 action. */
    public static void setNumberFour(int key) {
        getPrefs().putInteger(PREF_NUMBER_4, key);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param isKey Set whether PREF_NUMBER_4 action is key. */
    public static void setNumberFourIsKey(boolean isKey) {
        getPrefs().putBoolean(PREF_4_IS_KEY, isKey);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param key Set the key that triggers PREF_NUMBER_5 action. */
    public static void setNumberFive(int key) {
        getPrefs().putInteger(PREF_NUMBER_5, key);
        // Write to disk.
        getPrefs().flush();
    }

    /** @param isKey Set whether PREF_NUMBER_5 action is key. */
    public static void setNumberFiveIsKey(boolean isKey) {
        getPrefs().putBoolean(PREF_5_IS_KEY, isKey);
        // Write to disk.
        getPrefs().flush();
    }


    /** @return Key that triggers MOVE_UP action. */
    public static int getUpKey() {
        return getPrefs().getInteger(PREF_MOVE_UP_KEY, Input.Keys.W);
    }

    /** @return Whether MOVE_UP action is key. */
    public static boolean getUpKeyIsKey() {
        return getPrefs().getBoolean(PREF_UP_KEY_IS_KEY, true);
    }

    /** @return Key that triggers MOVE_DOWN action. */
    public static int getDownKey() {
        return getPrefs().getInteger(PREF_MOVE_DOWN_KEY, Input.Keys.S);
    }

    /** @return Whether MOVE_DOWN action is key. */
    public static boolean getDownKeyIsKey() {
        return getPrefs().getBoolean(PREF_DOWN_IS_KEY, true);
    }

    /** @return Key that triggers MOVE_LEFT action. */
    public static int getLeftKey() {
        return getPrefs().getInteger(PREF_MOVE_LEFT_KEY, Input.Keys.A);
    }

    /** @return Whether MOVE_LEFT action is key. */
    public static boolean getLeftKeyIsKey() {
        return getPrefs().getBoolean(PREF_LEFT_IS_KEY, true);
    }

    /** @return Key that triggers MOVE_RIGHT action. */
    public static int getRightKey() {
        return getPrefs().getInteger(PREF_MOVE_RIGHT_KEY, Input.Keys.D);
    }

    /** @return Whether MOVE_RIGHT action is key. */
    public static boolean getRightKeyIsKey() {
        return getPrefs().getBoolean(PREF_RIGHT_IS_KEY, true);
    }


    /** @return Key that triggers MOVE_RIGHT action. */
    public static int getLShiftKey() {
        return getPrefs().getInteger(PREF_L_SHIFT_KEY, Input.Keys.SHIFT_LEFT);
    }

    /** @return Whether MOVE_RIGHT action is key. */
    public static boolean getLShiftKeyIsKey() {
        return getPrefs().getBoolean(PREF_L_SHIFT_KEY_IS_KEY, true);
    }

    /** @return Key that triggers MOUSE_LEFT action. */
    public static int getMouseLeft() {
        return getPrefs().getInteger(PREF_MOUSE_LEFT, Input.Buttons.LEFT);
    }

    /** @return Whether MOUSE_LEFT action is key. */
    public static boolean getButtonLeftIsKey() {
        return getPrefs().getBoolean(PREF_MOUSE_LEFT_IS_KEY, false);
    }

    /** @return Key that triggers MOUSE_RIGHT action. */
    public static int getMouseRight() {
        return getPrefs().getInteger(PREF_MOUSE_RIGHT, Input.Buttons.RIGHT);
    }

    /** @return Whether MOUSE_RIGHT action is key. */
    public static boolean getButtonRightIsKey() {
        return getPrefs().getBoolean(PREF_MOUSE_RIGHT_IS_KEY, false);
    }

    /** @return Key that triggers NUMBER_1 action. */
    public static int getNumberOne() {
        return getPrefs().getInteger(PREF_NUMBER_1, Input.Keys.NUM_1);
    }

    /** @return Whether NUMBER_1 action is key. */
    public static boolean getNumberOneIsKey() {
        return getPrefs().getBoolean(PREF_1_IS_KEY, true);
    }

    /** @return Key that triggers NUMBER_2 action. */
    public static int getNumberTwo() {
        return getPrefs().getInteger(PREF_NUMBER_2, Input.Keys.NUM_2);
    }

    /** @return Whether NUMBER_2 action is key. */
    public static boolean getNumberTwoIsKey() {
        return getPrefs().getBoolean(PREF_2_IS_KEY, true);
    }

    /** @return Key that triggers NUMBER_3 action. */
    public static int getNumberThree() {
        return getPrefs().getInteger(PREF_NUMBER_3, Input.Keys.NUM_3);
    }

    /** @return Whether NUMBER_3 action is key. */
    public static boolean getNumberThreeIsKey() {
        return getPrefs().getBoolean(PREF_3_IS_KEY, true);
    }

    /** @return Key that triggers NUMBER_4 action. */
    public static int getNumberFour() {
        return getPrefs().getInteger(PREF_NUMBER_4, Input.Keys.NUM_4);
    }

    /** @return Whether NUMBER_4 action is key. */
    public static boolean getNumberFourIsKey() {
        return getPrefs().getBoolean(PREF_4_IS_KEY, true);
    }

    /** @return Key that triggers NUMBER_5 action. */
    public static int getNumberFive() {
        return getPrefs().getInteger(PREF_NUMBER_5, Input.Keys.NUM_5);
    }

    /** @return Whether NUMBER_5 action is key. */
    public static boolean getNumberFiveIsKey() {
        return getPrefs().getBoolean(PREF_5_IS_KEY, true);
    }

    /** @return Key that triggers NUMBER_6 action. */
    public static int getNumberSix() {
        return getPrefs().getInteger(PREF_NUMBER_6, Input.Keys.NUM_6);
    }

    /** @return Whether NUMBER_6 action is key. */
    public static boolean getNumberSixIsKey() {
        return getPrefs().getBoolean(PREF_6_IS_KEY, true);
    }

    /**
     * Get the string form of given button.
     * @param inputButton that was clicked
     * @return the string representation
     */
    public static String stringRepresentation(int inputButton) {
        if (inputButton < 5) {
            return BUTTONS_LIST.get(inputButton);
        } else {
            return "Not a button";
        }
    }
}

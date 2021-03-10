package ee.taltech.voshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class AppPreferences {

    private static final String PREF_MUSIC_VOLUME = "volume";
    private static final String PREF_SOUND_VOL = "sound";
    private static final String PREFS_NAME = "voshooter";
    private static final String PREF_PLAYER_COUNT = "4";
    private static final String PREF_GAME_MODE = "gamemode";

    /** @return A preferences object containing the player's preferences */
    protected Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    /**
     * @return The desired music volume.
     */
    public float getMusicVolume() {
        return getPrefs().getFloat(PREF_MUSIC_VOLUME, 0.5f);
    }

    /**
     * @param volume The desired music volume.
     */
    public void setMusicVolume(float volume) {
        getPrefs().putFloat(PREF_MUSIC_VOLUME, volume);
        // Write to disk.
        getPrefs().flush();
    }

    /**
     * @return The sound volume.
     */
    public float getSoundVolume() {
        return getPrefs().getFloat(PREF_SOUND_VOL, 0.5f);
    }

    /**
     * @param volume The desired sound volume.
     */
    public void setSoundVolume(float volume) {
        getPrefs().putFloat(PREF_SOUND_VOL, volume);
        // Write to disk.
        getPrefs().flush();
    }

    /**
     * @param players the desired player amount
     */
    public void setPlayerCount(float players) {
        getPrefs().putFloat(PREF_PLAYER_COUNT, players);
        // Write to disk.
        getPrefs().flush();
    }

    /**
     * @return the player count
     */
    public float getPlayerCount() {
        return getPrefs().getFloat(PREF_PLAYER_COUNT, 4);
    }

    /**
     * @param gameMode the desired gamemode
     */
    public void setGameMode(float gameMode) {
        getPrefs().putFloat(PREF_GAME_MODE, gameMode);
        // Write to disk.
        getPrefs().flush();
    }

    /**
     * @return the game mode
     */
    public float getGameMode() {
        return getPrefs().getFloat(PREF_GAME_MODE, 1);
    }
}

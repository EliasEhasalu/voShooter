package ee.taltech.voshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class AppPreferences {

    private static final String PREF_MUSIC_VOLUME = "volume";
    private static final String PREF_SOUND_VOL = "sound";
    private static final String PREFS_NAME = "voshooter";

    protected Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    /**
     * @return Get the music volume.
     */
    public float getMusicVolume() {
        return getPrefs().getFloat(PREF_MUSIC_VOLUME, 0.5f);
    }

    /**
     * @param volume Set the music volume.
     */
    public void setMusicVolume(float volume) {
        getPrefs().putFloat(PREF_MUSIC_VOLUME, volume);
        // Write to disk.
        getPrefs().flush();
    }

    /**
     * @return Get the sound volume.
     */
    public float getSoundVolume() {
        return getPrefs().getFloat(PREF_SOUND_VOL, 0.5f);
    }

    /**
     * @param volume Set the sound volume.
     */
    public void setSoundVolume(float volume) {
        getPrefs().putFloat(PREF_SOUND_VOL, volume);
        // Write to disk.
        getPrefs().flush();
    }
}

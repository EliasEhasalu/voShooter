package ee.taltech.voshooter.soundeffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import ee.taltech.voshooter.AppPreferences;

import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {

    private static Map<String, Sound> soundMap = new HashMap<>();

    /**
     * Play a sound effect.
     * @param path The path to the sound file.
     */
    public static void play(String path) {
        if (soundMap.containsKey(path)) {
            soundMap.get(path).play(AppPreferences.getSoundVolume());
        } else {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
            soundMap.put(path, sound);
            sound.play(AppPreferences.getSoundVolume());
        }
    }

    /**
     * Clear all of the entries in the sound map.
     */
    public static void dispose() {
        soundMap.values().forEach(Sound::dispose);
        soundMap.clear();
    }
}

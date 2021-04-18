package ee.taltech.voshooter.soundeffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
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
     * Play a sound effect at a location.
     * @param path
     * @param pos
     */
    public static void play(String path, Vector2 pos) {

    }

    /**
     * Clear all of the entries in the sound map.
     */
    public static void dispose() {
        soundMap.values().forEach(Sound::dispose);
        soundMap.clear();
    }
}

package ee.taltech.voshooter.soundeffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.AppPreferences;

import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {

    private static Map<String, Sound> soundMap = new HashMap<>();
    private static final float DEFAULT_MIN_DIST = 50f;
    private static final float DEFAULT_MAX_DIST = 2000f;

    /**
     * Play a sound effect.
     * @param path The path to the sound file.
     */
    public static void play(String path) {
        playSound(path, 1f);
    }

    /**
     * Play a sound effect with attenuation.
     * @param path The path to the sound file.
     * @param listenerPos The point to listen from.
     * @param senderPos The point that made the sound.
     */
    public static void play(String path, Vector2 listenerPos, Vector2 senderPos) {
        playSound(path, getAttenuation(listenerPos, senderPos, DEFAULT_MIN_DIST, DEFAULT_MAX_DIST));
    }

    /**
     * Play a sound effect with attenuation with custom min and max distances.
     * @param path The path to the sound file.
     * @param listenerPos The point to listen from.
     * @param senderPos The point that made the sound.
     * @param minDist Distance at which the sound will play at 100%.
     * @param maxDist Distance at which the sound will not play at all.
     */
    public static void play(String path, Vector2 listenerPos, Vector2 senderPos, float minDist, float maxDist) {
        playSound(path, getAttenuation(listenerPos, senderPos, minDist, maxDist));
    }

    /**
     * Play the sound effect.
     * @param path Path to the sound file.
     * @param attenuation The amount to decrease the sound volume.
     */
    private static void playSound(String path, float attenuation) {
        if (soundMap.containsKey(path)) {
            soundMap.get(path).play(AppPreferences.getSoundVolume() * attenuation);
        } else {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
            soundMap.put(path, sound);
            sound.play(AppPreferences.getSoundVolume() * attenuation);
        }
    }

    /**
     * Get the attenuation of the sound volume as a float from 0f to 1f.
     * @param listenerPos The point to listen from.
     * @param senderPos The point where the sound originates from.
     * @param minDist Distance after which the sound will play at 100% volume.
     * @param maxDist Distance after which the sound will not play at all.
     * @return Attenuation from 0f to 1f.
     */
    private static float getAttenuation(Vector2 listenerPos, Vector2 senderPos, float minDist, float maxDist) {
        final float dist = listenerPos.dst(senderPos);
        return Math.min(Math.max(1f - (dist - minDist) / (maxDist - minDist), 0f), 1f);
    }

    /**
     * Clear all of the entries in the sound map.
     */
    public static void dispose() {
        soundMap.values().forEach(Sound::dispose);
        soundMap.clear();
    }
}

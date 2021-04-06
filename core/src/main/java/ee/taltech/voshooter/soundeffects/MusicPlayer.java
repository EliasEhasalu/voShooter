package ee.taltech.voshooter.soundeffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import java.util.Objects;

public class MusicPlayer {

    private static Music mp3Music;
    private static float volume;
    private static String song;

    /**
     * Set the music to be played.
     * @param fileName  Name of the file to switch the music to.
     */
    public static void setMusic(String fileName) {
        if (!Objects.equals(song, fileName)) {
            if (mp3Music != null) {
                mp3Music.stop();
            }
            song = fileName;
            mp3Music = Gdx.audio.newMusic(Gdx.files.internal(fileName));
            mp3Music.setVolume(volume);
            mp3Music.play();
        }
    }

    /**
     * Set the volume of the music.
     * @param musicVolume New volume to set the music to.
     */
    public static void setVolume(float musicVolume) {
        volume = musicVolume;
        mp3Music.setVolume(musicVolume / 1.5f);
    }

    /**
     * Stop the music.
     */
    public static void stopMusic() {
        mp3Music.stop();
    }

    /**
     * Get the current song that is playing.
     * @return Song that is playing
     */
    public static String getMusic() {
        return song;
    }
}

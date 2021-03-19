package ee.taltech.voshooter.soundeffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicPlayer {

    private static Music mp3Music;
    private static float volume;

    /**
     * Set the music to be played.
     * @param fileName  Name of the file to switch the music to.
     */
    public static void setMusic(String fileName) {
        if (mp3Music != null) {
            mp3Music.stop();
        }
        mp3Music = Gdx.audio.newMusic(Gdx.files.internal(fileName));
        mp3Music.setVolume(volume);
        mp3Music.play();
    }

    /**
     * Set the volume of the music.
     * @param musicVolume New volume to set the music to.
     */
    public static void setVolume(float musicVolume) {
        volume = musicVolume;
        mp3Music.setVolume(musicVolume / 1.3f);
    }
}

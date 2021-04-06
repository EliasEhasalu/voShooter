package ee.taltech.voshooter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ee.taltech.voshooter.VoShooter;

public class DesktopLauncher {

    /**
     * Main entry to desktop launcher.
     *
     * @param args Not yet sure if the args are useful or just a PSVM requirement.
     */
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1600;
        config.height = 900;
        new LwjglApplication(new VoShooter(args), config);
    }
}

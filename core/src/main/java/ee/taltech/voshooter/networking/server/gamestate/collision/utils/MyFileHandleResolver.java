package ee.taltech.voshooter.networking.server.gamestate.collision.utils;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;

public class MyFileHandleResolver implements FileHandleResolver {

    /**
     * Get the file handle.
     * @param fileName The name of the file.
     * @return The file handle.
     */
    @Override
    public FileHandle resolve(String fileName) {
        return new FileHandle(new File(fileName));
    }
}

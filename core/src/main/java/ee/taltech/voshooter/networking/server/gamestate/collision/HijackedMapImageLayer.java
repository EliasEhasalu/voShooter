package ee.taltech.voshooter.networking.server.gamestate.collision;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;

public class HijackedMapImageLayer extends MapLayer {

    private float x;
    private float y;

    public HijackedMapImageLayer (float x, float y) {
        this.x = x;
        this.y = y;
    }

    public TextureRegion getTextureRegion () {
        return null;
    }

    public void setTextureRegion (TextureRegion region) {

    }

    public float getX () {
        return x;
    }

    public void setX (float x) {
        this.x = x;
    }

    public float getY () {
        return y;
    }

    public void setY (float y) {
        this.y = y;
    }

}

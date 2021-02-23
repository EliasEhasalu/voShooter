package ee.taltech.voshooter.rendering;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;

public class Renderer {

    private List<Drawable> drawables = new ArrayList<>();
    private Batch batch;

    /**
     * Construct this renderer with the given drawables.
     * @param batch The batch to draw onto.
     * @param drawables The list of drawables to keep track of.
     */
    public Renderer(Batch batch, List<Drawable> drawables) {
        this.batch = batch;
        this.drawables = drawables;
    }

    /**
     * Draw all objects allocated to this renderer.
     */
    public void draw() {
        for (Drawable d : drawables) d.getSprite().draw(batch);
    }

    /**
     * Allocate a drawable to this renderer.
     * @param d The drawable object to be added.
     * @return Whether the object was added.
     */
    public boolean addDrawable(Drawable d) {
        if (!drawables.contains(d)) {
            drawables.add(d);
            return true;
        }
        return false;
    }
}

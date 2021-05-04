package ee.taltech.voshooter.networking.server.gamestate.collision.utils;

import com.badlogic.gdx.math.Vector2;

public final class PixelToSimulation {

    public static final float PIXELS_PER_UNIT = 32f;

    /**
     * Convert pixels (from map file) to simulation units (for physics simulation).
     * @param pixels The amount of pixels.
     * @return The corresponding amount of units in simulation.
     */
    public static float toUnits(float pixels) {
        return pixels / PIXELS_PER_UNIT;
    }

    private static int castToGrid(float units) {
        return (int) Math.max(0, Math.floor(units));
    }

    /**
     * Convert a pixel vector to simulation vector.
     * @param pixels The pixel vector.
     * @return The simulation vector.
     */
    public static Vector2 toUnits(Vector2 pixels) {
        return new Vector2(toUnits(pixels.x), toUnits(pixels.y));
    }

    /** Convert to units, but use integer division (round down to nearest multiple of 32px). **/
    public static int[] castToGrid(Vector2 units) {
        return new int[] {castToGrid(units.x), castToGrid(units.y)};
    }

    /**
     * Convert simulation units to pixel units.
     * @param units The amount of simulation units.
     * @return The corresponding amount of pixels.
     */
    public static float toPixels(float units) {
        return units * PIXELS_PER_UNIT;
    }

    /**
     * Convert a simulation vector to a pixel vector.
     * @param units The simulation vector
     * @return The pixel vector.
     */
    public static Vector2 toPixels(Vector2 units) {
        return new Vector2(toPixels(units.x), toPixels(units.y));
    }
}

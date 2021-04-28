package ee.taltech.voshooter.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.voshooter.AppPreferences;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ParticleManager {

    private final Set<ParticleEffect> particleEffects = ConcurrentHashMap.newKeySet();
    private final Set<ParticleEffect> uiParticles = ConcurrentHashMap.newKeySet();

    /**
     * Add a new particle effect.
     * @param pos Position of the particle effect.
     * @param looping If the particle is looping or not.
     * @param path Path to the particle effect in assets.
     * @param isUI If the particle should be rendered on the UI.
     */
    public void addParticleEffect(Vector2 pos, String path, boolean looping, boolean isUI) {
        if (AppPreferences.getParticlesOn()) {
            ParticleEffect pe = new ParticleEffect();
            pe.load(Gdx.files.internal(path), Gdx.files.internal("textures/particles"));
            pe.setPosition(pos.x, pos.y);
            pe.start();

            if (isUI) {
                uiParticles.add(pe);
            } else {
                particleEffects.add(pe);
            }
        }
    }

    /**
     * Add particle effect on a line.
     * @param pos The start position of the line.
     * @param endPos The end position of the line.
     * @param path The path to the particle effect in assets.
     */
    public void addParticleEffect(Vector2 pos, Vector2 endPos, String path) {
        if (AppPreferences.getParticlesOn()) {
            ParticleEffect pe = new ParticleEffect();
            pe.load(Gdx.files.internal(path), Gdx.files.internal("textures/particles"));
            pe.setPosition(pos.x, pos.y);

            final float rotation = endPos.cpy().sub(pos).angleDeg();

            for (ParticleEmitter em : pe.getEmitters()) {
                if (em.getSpawnShape().getShape() == ParticleEmitter.SpawnShape.line) {
                    final float dist = new Vector2(0, 0).dst(new Vector2(em.getSpawnWidth().getHighMax(),
                            em.getSpawnHeight().getHighMax()));

                    em.getSpawnWidth().setHigh(endPos.x - pos.x);
                    em.getSpawnHeight().setHigh(endPos.y - pos.y);

                    final float emission = em.getEmission().getHighMax() / dist * pos.dst(endPos);
                    final float count = em.getMaxParticleCount() / dist * pos.dst(endPos);
                    em.getEmission().setHigh(emission);
                    em.setMaxParticleCount((int) count);
                } else if (em.getSpawnShape().getShape() == ParticleEmitter.SpawnShape.point) {
                    em.getAngle().setHighMin(em.getAngle().getHighMin() + rotation);
                    em.getAngle().setHighMax(em.getAngle().getHighMax() + rotation);
                    em.getAngle().setLowMin(em.getAngle().getLowMin() + rotation);
                    em.getAngle().setLowMax(em.getAngle().getLowMax() + rotation);
                }
            }

            pe.start();
            particleEffects.add(pe);
        }
    }

    /**
     * Remove particle effects that have finished.
     * @param pe Particle effect to remove.
     */
    public void particleEffectFinished(ParticleEffect pe) {
        particleEffects.remove(pe);
        uiParticles.remove(pe);
        pe.dispose();
    }

    /** @return Set of particle effects currently in the game. */
    public Set<ParticleEffect> getParticleEffects() {
        return particleEffects;
    }

    /** @return Set of the UI particles. */
    public Set<ParticleEffect> getUiParticles() {
        return uiParticles;
    }
}

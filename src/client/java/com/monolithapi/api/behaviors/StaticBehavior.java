package com.monolithapi.api.behaviors;

import com.monolithapi.api.IParticleBehavior;
import com.monolithapi.client.ThreedParticle;
import net.minecraft.util.math.Vec3d;

public class StaticBehavior implements IParticleBehavior {
    private final boolean fadeOut;

    public StaticBehavior() {
        this(true);
    }

    public StaticBehavior(boolean fadeOut) {
        this.fadeOut = fadeOut;
    }

    @Override
    public void tick(ThreedParticle particle) {
    }

    @Override
    public Vec3d getPosition(ThreedParticle particle, Vec3d center, float tickDelta) {
        return center;
    }

    @Override
    public void onRender(ThreedParticle particle, float tickDelta) {
        if (!fadeOut) return;
        float progress = particle.getProgress(tickDelta);
        if (progress > 0.75f) {
            particle.alpha = 1.0f - ((progress - 0.75f) / 0.25f);
        }
    }
}

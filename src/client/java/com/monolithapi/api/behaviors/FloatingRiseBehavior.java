package com.monolithapi.api.behaviors;

import com.monolithapi.api.IParticleBehavior;
import com.monolithapi.client.ThreedParticle;
import net.minecraft.util.math.Vec3d;

public class FloatingRiseBehavior implements IParticleBehavior {
    private final double verticalSpeed;
    private final double swayAmplitude;
    private final double swayFrequency;
    private final double phaseOffset;

    private double currentY = 0.0;
    private double prevY = 0.0;

    public FloatingRiseBehavior(double verticalSpeed, double swayAmplitude, double swayFrequency, double phaseOffset) {
        this.verticalSpeed = verticalSpeed;
        this.swayAmplitude = swayAmplitude;
        this.swayFrequency = swayFrequency;
        this.phaseOffset = phaseOffset;
    }

    @Override
    public void tick(ThreedParticle particle) {
        this.prevY = this.currentY;
        this.currentY += verticalSpeed;
    }

    @Override
    public Vec3d getPosition(ThreedParticle particle, Vec3d center, float tickDelta) {
        double smoothY = this.prevY + (this.currentY - this.prevY) * tickDelta;
        double smoothAge = particle.age + tickDelta;
        double offsetX = Math.sin(smoothAge * swayFrequency + phaseOffset) * swayAmplitude;
        double offsetZ = Math.cos(smoothAge * swayFrequency * 0.8 + phaseOffset) * swayAmplitude;

        return center.add(offsetX, smoothY, offsetZ);
    }

    @Override
    public void onRender(ThreedParticle particle, float tickDelta) {
        float progress = particle.getProgress(tickDelta);
        if (progress < 0.2f) {
            particle.alpha = progress / 0.2f;
        } else if (progress > 0.7f) {
            particle.alpha = 1.0f - ((progress - 0.7f) / 0.3f);
        } else {
            particle.alpha = 1.0f;
        }
    }
}

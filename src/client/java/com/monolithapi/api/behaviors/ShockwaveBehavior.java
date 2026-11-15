package com.monolithapi.api.behaviors;

import com.monolithapi.api.IParticleBehavior;
import com.monolithapi.client.ThreedParticle;
import net.minecraft.util.math.Vec3d;

public class ShockwaveBehavior implements IParticleBehavior {
    private final double maxRadius;
    private final double expansionSpeed;
    private final double angle;

    private double currentRadius = 0.0;
    private double prevRadius = 0.0;

    public ShockwaveBehavior(double maxRadius, double expansionSpeed, double angleDegrees) {
        this.maxRadius = maxRadius;
        this.expansionSpeed = expansionSpeed;
        this.angle = Math.toRadians(angleDegrees);
    }

    @Override
    public void tick(ThreedParticle particle) {
        this.prevRadius = this.currentRadius;

        if (this.currentRadius < maxRadius) {
            // Эффект затухания скорости при приближении к границе
            double remaining = maxRadius - this.currentRadius;
            double step = expansionSpeed * (remaining / maxRadius + 0.1);

            // Не даем радиусу превысить maxRadius
            this.currentRadius = Math.min(maxRadius, this.currentRadius + step);
        }
    }

    @Override
    public Vec3d getPosition(ThreedParticle particle, Vec3d center, float tickDelta) {
        double smoothRadius = this.prevRadius + (this.currentRadius - this.prevRadius) * tickDelta;

        double x = center.x + smoothRadius * Math.cos(angle);
        double z = center.z + smoothRadius * Math.sin(angle);

        return new Vec3d(x, center.y, z);
    }

    @Override
    public void onRender(ThreedParticle particle, float tickDelta) {
        if (maxRadius <= 0) return;

        // Прогресс расширения волны от 0.0 (центр) до 1.0 (край)
        double smoothRadius = this.prevRadius + (this.currentRadius - this.prevRadius) * tickDelta;
        float radiusProgress = (float) Math.clamp(smoothRadius / maxRadius, 0.0, 1.0);

        // Волна ярко вспыхивает в начале и плавно растворяется на последних 30% пути
        if (radiusProgress > 0.7f) {
            particle.alpha = 1.0f - ((radiusProgress - 0.7f) / 0.3f);
        } else {
            particle.alpha = 1.0f;
        }
    }
}

package com.monolithapi.api.behaviors;

import com.monolithapi.api.IParticleBehavior;
import com.monolithapi.client.ThreedParticle;
import net.minecraft.util.math.Vec3d;

public class HelixBehavior implements IParticleBehavior {
    private final double radius;
    private final double rotationSpeed;
    private final double verticalSpeed;
    private final double maxHeight;

    private double angle;
    private double prevAngle;

    private double currentYOffset = 0.0;
    private double prevYOffset = 0.0;

    public HelixBehavior(double radius, double rotationSpeed, double verticalSpeed, double maxHeight) {
        this(radius, rotationSpeed, verticalSpeed, maxHeight, 0.0);
    }

    public HelixBehavior(double radius, double rotationSpeed, double verticalSpeed, double maxHeight, double startAngle) {
        this.radius = radius;
        this.rotationSpeed = rotationSpeed;
        this.verticalSpeed = verticalSpeed;
        this.maxHeight = maxHeight;
        this.angle = startAngle;
        this.prevAngle = startAngle;
    }

    @Override
    public void tick(ThreedParticle particle) {
        this.prevAngle = this.angle;
        this.prevYOffset = this.currentYOffset;
        this.angle += rotationSpeed;
        this.currentYOffset += verticalSpeed;
        if (this.angle > Math.PI * 2) {
            this.angle -= Math.PI * 2;
            this.prevAngle -= Math.PI * 2;
        }
        if (this.currentYOffset > maxHeight) {
            this.currentYOffset = 0.0;
            this.prevYOffset = 0.0;
        }
    }

    @Override
    public Vec3d getPosition(ThreedParticle particle, Vec3d center, float tickDelta) {
        double smoothAngle = this.prevAngle + (this.angle - this.prevAngle) * tickDelta;
        double smoothYOffset = this.prevYOffset + (this.currentYOffset - this.prevYOffset) * tickDelta;
        double x = center.x + radius * Math.cos(smoothAngle);
        double z = center.z + radius * Math.sin(smoothAngle);
        double y = center.y + smoothYOffset;

        return new Vec3d(x, y, z);
    }

    @Override
    public void onRender(ThreedParticle particle, float tickDelta) {
        if (maxHeight <= 0) return;
        double smoothYOffset = this.prevYOffset + (this.currentYOffset - this.prevYOffset) * tickDelta;
        float heightProgress = (float) Math.clamp(smoothYOffset / maxHeight, 0.0, 1.0);
        if (heightProgress < 0.15f) {
            particle.alpha = heightProgress / 0.15f;
        } else if (heightProgress > 0.8f) {
            particle.alpha = 1.0f - ((heightProgress - 0.8f) / 0.2f);
        } else {
            particle.alpha = 1.0f;
        }
    }
}

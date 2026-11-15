package com.monolithapi.api.behaviors;

import com.monolithapi.api.IParticleBehavior;
import com.monolithapi.client.ThreedParticle;
import net.minecraft.util.math.Vec3d;

public class OrbitBehavior implements IParticleBehavior {
    private final double radius;
    private final double speed;
    private double angle;
    private double prevAngle;

    public OrbitBehavior(double radius, double speed) {
        this(radius, speed, 0.0);
    }

    public OrbitBehavior(double radius, double speed, double startAngle) {
        this.radius = radius;
        this.speed = speed;
        this.angle = startAngle;
        this.prevAngle = startAngle;
    }

    @Override
    public void tick(ThreedParticle particle) {
        this.prevAngle = this.angle;
        this.angle += speed;

        if (this.angle > Math.PI * 2) {
            this.angle -= Math.PI * 2;
            this.prevAngle -= Math.PI * 2;
        }
    }

    @Override
    public Vec3d getPosition(ThreedParticle particle, Vec3d center, float tickDelta) {
        double smoothAngle = this.prevAngle + (this.angle - this.prevAngle) * tickDelta;

        double x = center.x + radius * Math.cos(smoothAngle);
        double z = center.z + radius * Math.sin(smoothAngle);
        double y = center.y + Math.sin(smoothAngle * 2.0) * 0.15;

        return new Vec3d(x, y, z);
    }

    @Override
    public void onRender(ThreedParticle particle, float tickDelta) {
        float progress = particle.getProgress(tickDelta);
        if (progress > 0.8f) {
            particle.alpha = 1.0f - ((progress - 0.8f) / 0.2f);
        }
    }
}

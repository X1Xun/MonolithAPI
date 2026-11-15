package com.monolithapi.api;

import com.monolithapi.client.ThreedParticle;
import com.monolithapi.client.ParticleManager;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class MonolithAPI {

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private Vec3d center = Vec3d.ZERO;
        private IParticleBehavior behavior;
        private float size = 0.5f;
        private Vector3f color = new Vector3f(1f, 1f, 1f);
        private int maxAge = 200;

        public Builder at(Vec3d pos) { this.center = pos; return this; }
        public Builder behavior(IParticleBehavior behavior) { this.behavior = behavior; return this; }
        public Builder size(float size) { this.size = size; return this; }
        public Builder color(float r, float g, float b) { this.color = new Vector3f(r, g, b); return this; }
        public Builder lifetime(int ticks) { this.maxAge = ticks; return this; }

        public void spawn() {
            if (this.behavior == null) {
                throw new IllegalStateException("Поведение не задано");
            }
            ThreedParticle particle = new ThreedParticle(this.center, this.behavior, this.size, this.color, this.maxAge);
            ParticleManager.INSTANCE.addParticle(particle);
        }
    }
}

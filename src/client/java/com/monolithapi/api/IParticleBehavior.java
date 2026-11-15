package com.monolithapi.api;

import com.monolithapi.client.ThreedParticle;
import net.minecraft.util.math.Vec3d;

public interface IParticleBehavior {

    default void onInit(ThreedParticle particle) {}

    void tick(ThreedParticle particle);

    Vec3d getPosition(ThreedParticle particle, Vec3d center, float tickDelta);

    default void onRender(ThreedParticle particle, float tickDelta) {}
}

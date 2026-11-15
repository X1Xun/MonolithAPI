package com.monolithapi.api.methods;

import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class Particle3DShapes {
    public static void spawnCube(
            Vec3d center,
            double size,
            int particlesPerEdge,
            float minSize,
            float maxSize,
            Vector3f color,
            int lifetime
    ) {
        double half = size / 2.0;
        Vec3d b1 = center.add(-half, -half, -half);
        Vec3d b2 = center.add( half, -half, -half);
        Vec3d b3 = center.add( half, -half,  half);
        Vec3d b4 = center.add(-half, -half,  half);
        Vec3d t1 = center.add(-half,  half, -half);
        Vec3d t2 = center.add( half,  half, -half);
        Vec3d t3 = center.add( half,  half,  half);
        Vec3d t4 = center.add(-half,  half,  half);
        ParticleLine.spawnSizedParticleLine(b1, b2, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(b2, b3, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(b3, b4, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(b4, b1, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(t1, t2, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(t2, t3, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(t3, t4, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(t4, t1, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(b1, t1, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(b2, t2, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(b3, t3, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(b4, t4, particlesPerEdge, minSize, maxSize, color, lifetime);
    }
    public static void spawnPyramid(
            Vec3d baseCenter,
            double baseSize,
            double height,
            int particlesPerEdge,
            float minSize,
            float maxSize,
            Vector3f color,
            int lifetime
    ) {
        double half = baseSize / 2.0;

        Vec3d b1 = baseCenter.add(-half, 0, -half);
        Vec3d b2 = baseCenter.add( half, 0, -half);
        Vec3d b3 = baseCenter.add( half, 0,  half);
        Vec3d b4 = baseCenter.add(-half, 0,  half);
        Vec3d apex = baseCenter.add(0, height, 0);
        ParticleLine.spawnSizedParticleLine(b1, b2, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(b2, b3, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(b3, b4, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(b4, b1, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(b1, apex, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(b2, apex, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(b3, apex, particlesPerEdge, minSize, maxSize, color, lifetime);
        ParticleLine.spawnSizedParticleLine(b4, apex, particlesPerEdge, minSize, maxSize, color, lifetime);
    }
}

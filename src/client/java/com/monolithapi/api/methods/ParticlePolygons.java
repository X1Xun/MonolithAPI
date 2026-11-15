package com.monolithapi.api.methods;

import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ParticlePolygons {
    public static void spawnPolygon(
            Vec3d center,
            double radius,
            int sides,
            double pitchDegrees,
            double yawDegrees,
            int particlesPerEdge,
            float minSize,
            float maxSize,
            Vector3f color,
            int lifetime
    ) {
        if (sides < 3 || particlesPerEdge <= 0) return;

        Vec3d[] vertices = new Vec3d[sides];

        Quaternionf rotation = new Quaternionf()
                .rotateY((float) Math.toRadians(-yawDegrees))
                .rotateX((float) Math.toRadians(pitchDegrees));

        for (int i = 0; i < sides; i++) {
            double angle = (2.0 * Math.PI * i) / sides;

            Vector3f localPos = new Vector3f(
                    (float) (radius * Math.cos(angle)),
                    0.0f,
                    (float) (radius * Math.sin(angle))
            );

            rotation.transform(localPos);

            vertices[i] = center.add(localPos.x, localPos.y, localPos.z);
        }

        for (int i = 0; i < sides; i++) {
            Vec3d start = vertices[i];
            Vec3d end = vertices[(i + 1) % sides];
            ParticleLine.spawnSizedParticleLine(start, end, particlesPerEdge, minSize, maxSize, color, lifetime);
        }
    }
    public static void spawnPolygon(Vec3d center, double radius, int sides, double yawDegrees, int particlesPerEdge, float minSize, float maxSize, Vector3f color, int lifetime) {
        spawnPolygon(center, radius, sides, 0.0, yawDegrees, particlesPerEdge, minSize, maxSize, color, lifetime);
    }
    public static void spawnStar(
            Vec3d center,
            double outerRadius,
            double innerRadius,
            int points,
            double pitchDegrees,
            double yawDegrees,
            int particlesPerEdge,
            float minSize,
            float maxSize,
            Vector3f color,
            int lifetime
    ) {
        if (points < 3 || particlesPerEdge <= 0) return;

        int totalVertices = points * 2;
        Vec3d[] vertices = new Vec3d[totalVertices];

        Quaternionf rotation = new Quaternionf()
                .rotateY((float) Math.toRadians(-yawDegrees))
                .rotateX((float) Math.toRadians(pitchDegrees));

        for (int i = 0; i < totalVertices; i++) {
            double angle = (Math.PI * i) / points;
            double r = (i % 2 == 0) ? outerRadius : innerRadius;

            Vector3f localPos = new Vector3f(
                    (float) (r * Math.cos(angle)),
                    0.0f,
                    (float) (r * Math.sin(angle))
            );

            rotation.transform(localPos);
            vertices[i] = center.add(localPos.x, localPos.y, localPos.z);
        }

        for (int i = 0; i < totalVertices; i++) {
            Vec3d start = vertices[i];
            Vec3d end = vertices[(i + 1) % totalVertices];
            ParticleLine.spawnSizedParticleLine(start, end, particlesPerEdge, minSize, maxSize, color, lifetime);
        }
    }
    public static void spawnTriangle(Vec3d center, double radius, double pitch, double yaw, int count, float minSize, float maxSize, Vector3f color, int lifetime) {
        spawnPolygon(center, radius, 3, pitch, yaw, count, minSize, maxSize, color, lifetime);
    }

    public static void spawnSquare(Vec3d center, double radius, double pitch, double yaw, int count, float minSize, float maxSize, Vector3f color, int lifetime) {
        spawnPolygon(center, radius, 4, pitch, yaw + 45.0, count, minSize, maxSize, color, lifetime);
    }
}

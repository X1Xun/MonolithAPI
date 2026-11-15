package com.monolithapi.api.methods;

import com.monolithapi.api.ParticleAPI;
import com.monolithapi.api.behaviors.HelixBehavior;
import com.monolithapi.api.behaviors.OrbitBehavior;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class ParticleEffects {
    public static void spawnDoubleHelix(
            Vec3d baseCenter,
            double radius,
            double rotationSpeed,
            double verticalSpeed,
            double maxHeight,
            int particlesPerStrand,
            float size,
            Vector3f primaryColor,
            Vector3f secondaryColor,
            int lifetime
    ) {
        for (int i = 0; i < particlesPerStrand; i++) {
            double phaseOffset = (Math.PI * 2.0 * i) / particlesPerStrand;

            // Первая нить спирали
            ParticleAPI.create()
                    .at(baseCenter)
                    .behavior(new HelixBehavior(radius, rotationSpeed, verticalSpeed, maxHeight, phaseOffset))
                    .color(primaryColor.x, primaryColor.y, primaryColor.z)
                    .size(size)
                    .lifetime(lifetime)
                    .spawn();

            ParticleAPI.create()
                    .at(baseCenter)
                    .behavior(new HelixBehavior(radius, rotationSpeed, verticalSpeed, maxHeight, phaseOffset + Math.PI))
                    .color(secondaryColor.x, secondaryColor.y, secondaryColor.z)
                    .size(size)
                    .lifetime(lifetime)
                    .spawn();
        }
    }
    public static void spawnAtomAura(Vec3d center, double radius, double speed, int particlesPerRing, float size, Vector3f color, int lifetime) {
        ParticleShapes.spawnOrbitRing(center, radius, speed, particlesPerRing, size, color, lifetime);
        for (int i = 0; i < particlesPerRing; i++) {
            double angle = (Math.PI * 2.0 * i) / particlesPerRing;
            Vec3d offset1 = new Vec3d(Math.cos(angle) * 0.3, Math.sin(angle) * 0.6, 0);
            Vec3d offset2 = new Vec3d(0, Math.sin(angle) * 0.6, Math.cos(angle) * 0.3);

            ParticleAPI.create()
                    .at(center.add(offset1))
                    .behavior(new OrbitBehavior(radius, -speed * 1.2, angle))
                    .color(color.x, color.y, color.z)
                    .size(size)
                    .lifetime(lifetime)
                    .spawn();

            ParticleAPI.create()
                    .at(center.add(offset2))
                    .behavior(new OrbitBehavior(radius, speed * 0.8, angle + Math.PI / 2))
                    .color(color.x, color.y, color.z)
                    .size(size)
                    .lifetime(lifetime)
                    .spawn();
        }
    }
    public static void spawnGroundSlam(Vec3d impactPos, Vector3f mainColor, Vector3f accentColor, double radius) {
        // Быстрая внешняя волна
        ParticleShapes.spawnShockwave(impactPos, radius, 0.65, 48, 0.25f, mainColor, 29);
        // Медленная внутренняя волна
        ParticleShapes.spawnShockwave(impactPos.add(0, 0.1, 0), radius, 0.35, 32, 0.35f, accentColor, 35);
        // Восходящий вихрь в центре
//        spawnDoubleHelix(impactPos, 0.8, 0.25, 0.15, 3.0, 6, 0.2f, mainColor, accentColor, 40);
    }
}

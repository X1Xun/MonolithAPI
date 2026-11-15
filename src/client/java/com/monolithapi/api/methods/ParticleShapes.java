package com.monolithapi.api.methods;

import com.monolithapi.api.MonolithAPI;
import com.monolithapi.api.behaviors.OrbitBehavior;
import com.monolithapi.api.behaviors.ShockwaveBehavior;
import com.monolithapi.api.behaviors.StaticBehavior;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class ParticleShapes {
    public static void spawnSphere(Vec3d center, double radius, int count, float size, Vector3f color, int lifetime) {
        if (count <= 0) return;

        StaticBehavior behavior = new StaticBehavior();
        double goldenRatio = (1.0 + Math.sqrt(5.0)) / 2.0;

        for (int i = 0; i < count; i++) {
            // Распределение по высоте от -1 до 1
            double y = 1.0 - (2.0 * i) / (count - 1.0);
            double radiusAtY = Math.sqrt(Math.max(0.0, 1.0 - y * y));

            double theta = (2.0 * Math.PI * i) / goldenRatio;
            double x = Math.cos(theta) * radiusAtY;
            double z = Math.sin(theta) * radiusAtY;

            Vec3d pos = center.add(x * radius, y * radius, z * radius);

            MonolithAPI.create()
                    .at(pos)
                    .behavior(behavior)
                    .color(color.x, color.y, color.z)
                    .size(size)
                    .lifetime(lifetime)
                    .spawn();
        }
    }
    public static void spawnOrbitRing(Vec3d center, double radius, double speed, int count, float size, Vector3f color, int lifetime) {
        if (count <= 0) return;

        for (int i = 0; i < count; i++) {
            double startAngle = (2.0 * Math.PI * i) / count;

            MonolithAPI.create()
                    .at(center)
                    .behavior(new OrbitBehavior(radius, speed, startAngle))
                    .color(color.x, color.y, color.z)
                    .size(size)
                    .lifetime(lifetime)
                    .spawn();
        }
    }
    public static void spawnShockwave(Vec3d center, double maxRadius, double expansionSpeed, int count, float size, Vector3f color, int lifetime) {
        if (count <= 0) return;

        for (int i = 0; i < count; i++) {
            double angleDegrees = (360.0 * i) / count;

            MonolithAPI.create()
                    .at(center)
                    .behavior(new ShockwaveBehavior(maxRadius, expansionSpeed, angleDegrees))
                    .color(color.x, color.y, color.z)
                    .size(size)
                    .lifetime(lifetime)
                    .spawn();
        }
    }
}

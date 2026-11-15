package com.monolithapi.api.methods;

import com.monolithapi.api.ParticleAPI;
import com.monolithapi.api.behaviors.StaticBehavior;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class ParticleLine {

    public static void spawnSizedParticleLine(
            Vec3d start,
            Vec3d end,
            int count,
            float minSize,
            float maxSize,
            Vector3f color,
            int lifetime
    ) {
        if (count <= 0) return;

        StaticBehavior behavior = new StaticBehavior();

        if (count == 1) {
            Vec3d center = start.lerp(end, 0.5);
            ParticleAPI.create()
                    .at(center)
                    .behavior(behavior)
                    .color(color.x, color.y, color.z)
                    .size(minSize)
                    .lifetime(lifetime)
                    .spawn();
            return;
        }

        for (int i = 0; i < count; i++) {
            double progress = (double) i / (count - 1);

            // Встроенный в Vec3d метод линейной интерполяции вместо ручного расчета x, y, z
            Vec3d particlePos = start.lerp(end, progress);

            // 0.0 в центре линии, 1.0 на концах (старте и финише)
            double distanceFromCenter = Math.abs(progress - 0.5) * 2.0;
            float currentSize = minSize + (float) ((maxSize - minSize) * distanceFromCenter);

            ParticleAPI.create()
                    .at(particlePos)
                    .behavior(behavior)
                    .color(color.x, color.y, color.z)
                    .size(currentSize)
                    .lifetime(lifetime)
                    .spawn();
        }
    }
}

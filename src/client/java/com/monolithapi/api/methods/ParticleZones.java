package com.monolithapi.api.methods;

import com.monolithapi.api.MonolithAPI;
import com.monolithapi.api.behaviors.FloatingRiseBehavior;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3f;

public class ParticleZones {

    private static final Random RANDOM = Random.create();

    /**
     * Создает круглую зону у земли, в которой случайно появляются частицы и плавно летят вверх.
     * Вызывай этот метод каждый тик (или раз в несколько тиков), пока активна зона.
     *
     * @param groundCenter  Центр зоны на уровне земли
     * @param radius        Радиус зоны в блоках
     * @param count         Сколько частиц заспавнить за один вызов
     * @param minSpeed      Минимальная скорость подъема (например, 0.03)
     * @param maxSpeed      Максимальная скорость подъема (например, 0.08)
     * @param minSize       Минимальный размер частицы
     * @param maxSize       Максимальный размер частицы
     * @param color         Цвет частиц
     * @param minLifetime   Минимальное время жизни в тиках
     * @param maxLifetime   Максимальное время жизни в тиках
     */
    public static void spawnRisingZone(
            Vec3d groundCenter,
            double radius,
            int count,
            double minSpeed,
            double maxSpeed,
            float minSize,
            float maxSize,
            Vector3f color,
            int minLifetime,
            int maxLifetime
    ) {
        if (count <= 0) return;

        for (int i = 0; i < count; i++) {
            // Равномерное распределение точек по площади круга (через Math.sqrt)
            double r = radius * Math.sqrt(RANDOM.nextDouble());
            double angle = RANDOM.nextDouble() * Math.PI * 2.0;

            double spawnX = groundCenter.x + r * Math.cos(angle);
            double spawnZ = groundCenter.z + r * Math.sin(angle);
            // Небольшой разброс по высоте у самой земли (от 0.05 до 0.35 блока)
            double spawnY = groundCenter.y + 0.05 + RANDOM.nextDouble() * 0.3;

            Vec3d spawnPos = new Vec3d(spawnX, spawnY, spawnZ);

            // Случайные индивидуальные параметры для естественности
            double speed = minSpeed + RANDOM.nextDouble() * (maxSpeed - minSpeed);
            float size = minSize + RANDOM.nextFloat() * (maxSize - minSize);
            int lifetime = minLifetime + RANDOM.nextInt(Math.max(1, maxLifetime - minLifetime + 1));
            double phase = RANDOM.nextDouble() * Math.PI * 2.0;

            MonolithAPI.create()
                    .at(spawnPos)
                    .behavior(new FloatingRiseBehavior(speed, 0.25, 0.12, phase))
                    .color(color.x, color.y, color.z)
                    .size(size)
                    .lifetime(lifetime)
                    .spawn();
        }
    }
}

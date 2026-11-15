package com.monolithapi.api.behaviors;

import com.monolithapi.api.IParticleBehavior;
import com.monolithapi.api.path.IPathModifier;
import com.monolithapi.client.ThreedParticle;
import net.minecraft.util.math.Vec3d;

public class PathBehavior implements IParticleBehavior {
    private final Vec3d start;
    private final Vec3d end;
    private final IPathModifier modifier;

    private final Vec3d rightAxis;
    private final Vec3d upAxis;
    private final double totalDistance;

    public PathBehavior(Vec3d start, Vec3d end, IPathModifier modifier) {
        this.start = start;
        this.end = end;
        this.modifier = modifier != null ? modifier : IPathModifier.STRAIGHT;

        Vec3d diff = end.subtract(start);
        this.totalDistance = diff.length();

        if (this.totalDistance > 1e-6) {
            Vec3d forward = diff.normalize();
            // Выбираем вспомогательный вектор (если летим строго вертикально, берем ось X вместо Y)
            Vec3d worldUp = Math.abs(forward.y) > 0.99 ? new Vec3d(1, 0, 0) : new Vec3d(0, 1, 0);
            this.rightAxis = forward.crossProduct(worldUp).normalize();
            this.upAxis = this.rightAxis.crossProduct(forward).normalize();
        } else {
            this.rightAxis = Vec3d.ZERO;
            this.upAxis = Vec3d.ZERO;
        }
    }

    @Override
    public void tick(ThreedParticle particle) {
        // Движение управляется через progress жизни партикла в getPosition
    }

    @Override
    public Vec3d getPosition(ThreedParticle particle, Vec3d center, float tickDelta) {
        // Плавный прогресс от 0.0 (старт) до 1.0 (финиш) на основе maxAge
        float progress = particle.getProgress(tickDelta);

        // 1. Базовая точка на прямой линии
        Vec3d basePos = start.lerp(end, progress);

        // 2. Получаем локальное смещение от функции (синусоида, дуга и т.д.)
        Vec3d localOffset = modifier.getOffset(progress, totalDistance);

        // 3. Переводим локальное смещение в мировые 3D-координаты
        return basePos
                .add(rightAxis.multiply(localOffset.x))
                .add(upAxis.multiply(localOffset.y));
    }

    @Override
    public void onRender(ThreedParticle particle, float tickDelta) {
        // Плавное затухание в последние 15% пути перед попаданием в цель
        float progress = particle.getProgress(tickDelta);
        if (progress > 0.85f) {
            particle.alpha = 1.0f - ((progress - 0.85f) / 0.15f);
        }
    }
}

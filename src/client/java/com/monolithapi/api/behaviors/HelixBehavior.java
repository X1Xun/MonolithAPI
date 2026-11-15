package com.monolithapi.api.behaviors;

import com.monolithapi.api.IParticleBehavior;
import com.monolithapi.client.ThreedParticle;
import net.minecraft.util.math.Vec3d;

public class HelixBehavior implements IParticleBehavior {
    private final double radius;
    private final double rotationSpeed; // Скорость вращения (рад/тик)
    private final double verticalSpeed; // Скорость подъема вверх по оси Y
    private final double maxHeight;     // Максимальная высота подъема

    private double angle;
    private double prevAngle;

    private double currentYOffset = 0.0;
    private double prevYOffset = 0.0;

    public HelixBehavior(double radius, double rotationSpeed, double verticalSpeed, double maxHeight) {
        this(radius, rotationSpeed, verticalSpeed, maxHeight, 0.0);
    }

    public HelixBehavior(double radius, double rotationSpeed, double verticalSpeed, double maxHeight, double startAngle) {
        this.radius = radius;
        this.rotationSpeed = rotationSpeed;
        this.verticalSpeed = verticalSpeed;
        this.maxHeight = maxHeight;
        this.angle = startAngle;
        this.prevAngle = startAngle;
    }

    @Override
    public void tick(ThreedParticle particle) {
        // Запоминаем состояния прошлого тика для интерполяции кадров
        this.prevAngle = this.angle;
        this.prevYOffset = this.currentYOffset;

        // Наращиваем угол и высоту
        this.angle += rotationSpeed;
        this.currentYOffset += verticalSpeed;

        // Сброс угла для предотвращения переполнения
        if (this.angle > Math.PI * 2) {
            this.angle -= Math.PI * 2;
            this.prevAngle -= Math.PI * 2;
        }

        // Эффект циклического фонтана: если поднялись слишком высоко, начинаем снизу
        if (this.currentYOffset > maxHeight) {
            this.currentYOffset = 0.0;
            this.prevYOffset = 0.0;
        }
    }

    @Override
    public Vec3d getPosition(ThreedParticle particle, Vec3d center, float tickDelta) {
        // Плавный расчет промежуточных значений между тиками (Lerp)
        double smoothAngle = this.prevAngle + (this.angle - this.prevAngle) * tickDelta;
        double smoothYOffset = this.prevYOffset + (this.currentYOffset - this.prevYOffset) * tickDelta;

        // Математика винтовой траектории
        double x = center.x + radius * Math.cos(smoothAngle);
        double z = center.z + radius * Math.sin(smoothAngle);
        double y = center.y + smoothYOffset;

        return new Vec3d(x, y, z);
    }

    @Override
    public void onRender(ThreedParticle particle, float tickDelta) {
        if (maxHeight <= 0) return;

        // Вычисляем прогресс по высоте (от 0.0 у основания до 1.0 на пике)
        double smoothYOffset = this.prevYOffset + (this.currentYOffset - this.prevYOffset) * tickDelta;
        float heightProgress = (float) Math.clamp(smoothYOffset / maxHeight, 0.0, 1.0);

        // Плавное появление в нижних 15% и затухание в верхних 20%, чтобы сброс высоты был незаметным
        if (heightProgress < 0.15f) {
            particle.alpha = heightProgress / 0.15f;
        } else if (heightProgress > 0.8f) {
            particle.alpha = 1.0f - ((heightProgress - 0.8f) / 0.2f);
        } else {
            particle.alpha = 1.0f;
        }
    }
}

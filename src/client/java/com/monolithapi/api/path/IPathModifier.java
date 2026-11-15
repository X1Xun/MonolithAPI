package com.monolithapi.api.path;

import net.minecraft.util.math.Vec3d;

@FunctionalInterface
public interface IPathModifier {
    /**
     * Возвращает смещение (X = вправо/влево, Y = вверх/вниз) относительно прямой линии полета.
     *
     * @param progress Прогресс пути от 0.0 (точка старта) до 1.0 (точка финиша)
     * @param distance Полная длина прямой дистанции в блоках
     * @return Vec3d, где x — смещение по локальной оси Right, y — смещение по локальной оси Up
     */
    Vec3d getOffset(double progress, double distance);

    // Готовый пресет: чистая прямая без отклонений
    IPathModifier STRAIGHT = (progress, distance) -> Vec3d.ZERO;

    // Готовый пресет: синусоида в вертикальной плоскости
    static IPathModifier sineWave(double amplitude, double frequency) {
        return (progress, distance) -> {
            // Умножаем на sin(PI * progress), чтобы волна плавно затухала ровно в точках старта и финиша
            double envelope = Math.sin(Math.PI * progress);
            double wave = Math.sin(progress * Math.PI * 2.0 * frequency) * amplitude * envelope;
            return new Vec3d(0.0, wave, 0.0);
        };
    }

    // Готовый пресет: спираль вокруг прямой линии полета
    static IPathModifier spiral(double radius, double turns, double startAngle) {
        return (progress, distance) -> {
            double envelope = Math.sin(Math.PI * progress);
            double angle = startAngle + progress * Math.PI * 2.0 * turns;
            double offsetX = Math.cos(angle) * radius * envelope;
            double offsetY = Math.sin(angle) * radius * envelope;
            return new Vec3d(offsetX, offsetY, 0.0);
        };
    }
}

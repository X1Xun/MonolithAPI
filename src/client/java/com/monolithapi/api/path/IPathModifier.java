package com.monolithapi.api.path;

import net.minecraft.util.math.Vec3d;

@FunctionalInterface
public interface IPathModifier {
    Vec3d getOffset(double progress, double distance);
    IPathModifier STRAIGHT = (progress, distance) -> Vec3d.ZERO;
    static IPathModifier sineWave(double amplitude, double frequency) {
        return (progress, distance) -> {
            double envelope = Math.sin(Math.PI * progress);
            double wave = Math.sin(progress * Math.PI * 2.0 * frequency) * amplitude * envelope;
            return new Vec3d(0.0, wave, 0.0);
        };
    }
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

package com.monolithapi.client;

import com.monolithapi.api.IParticleBehavior;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

public class ThreedParticle {
    private Vec3d anchorCenter;
    private final IParticleBehavior behavior;

    public float size;
    public Vector3f color;
    public float alpha = 1.0f;

    public int age;
    public final int maxAge;

    // История позиций для шлейфа (ArrayDeque быстрее ArrayList при вставке в начало и удалении с конца)
    private final Deque<Vec3d> trailPositions = new ArrayDeque<>();
    private final int maxTrailLength = 12;

    public ThreedParticle(Vec3d anchorCenter, IParticleBehavior behavior, float size, Vector3f color, int maxAge) {
        this.anchorCenter = anchorCenter;
        this.behavior = behavior;
        this.size = size;
        this.color = color;
        this.maxAge = maxAge;
        this.age = 0;

        this.behavior.onInit(this);
    }

    public void tick() {
        this.age++;

        // Запоминаем позицию ДО тика поведения для плавного шлейфа
        this.trailPositions.addFirst(getInterpolatedPosition(0.0f));
        if (this.trailPositions.size() > maxTrailLength) {
            this.trailPositions.removeLast();
        }

        // Обновляем математику траектории
        this.behavior.tick(this);
    }

    public Vec3d getInterpolatedPosition(float tickDelta) {
        return this.behavior.getPosition(this, this.anchorCenter, tickDelta);
    }

    /**
     * Вызывается в рендерере перед отрисовкой партикла, чтобы поведение обновило size, color и alpha.
     */
    public void prepareRender(float tickDelta) {
        this.behavior.onRender(this, tickDelta);
    }

    /**
     * Возвращает прогресс жизни от 0.0f (старт) до 1.0f (смерть).
     */
    public float getProgress(float tickDelta) {
        if (this.maxAge <= 0) return 1.0f;
        return Math.min(1.0f, (this.age + tickDelta) / (float) this.maxAge);
    }

    public Collection<Vec3d> getTrailPositions() {
        return this.trailPositions;
    }

    public boolean isDead() {
        return this.age >= this.maxAge;
    }

    public Vec3d getAnchorCenter() {
        return anchorCenter;
    }

    public void setAnchorCenter(Vec3d anchorCenter) {
        this.anchorCenter = anchorCenter;
    }
}

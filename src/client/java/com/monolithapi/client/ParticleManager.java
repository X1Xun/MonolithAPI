package com.monolithapi.client;

import java.util.ArrayList;
import java.util.List;

public class ParticleManager {

    public static final ParticleManager INSTANCE = new ParticleManager();

    // Синхронизированный список, чтобы избежать крашей при многопоточном рендере
    private final List<ThreedParticle> particles = new ArrayList<>();

    private ParticleManager() {}

    public void addParticle(ThreedParticle particle) {
        synchronized (particles) {
            particles.add(particle);
        }
    }

    public void tick() {
        synchronized (particles) {
            particles.removeIf(ThreedParticle::isDead);
            for (ThreedParticle particle : particles) {
                particle.tick();
            }
        }
    }

    public List<ThreedParticle> getParticles() {
        return particles;
    }
}

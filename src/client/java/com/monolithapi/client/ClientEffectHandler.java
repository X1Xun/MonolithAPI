package com.monolithapi.client;

import com.monolithapi.api.MonolithAPI;
import com.monolithapi.api.behaviors.PathBehavior;
import com.monolithapi.api.methods.*;
import com.monolithapi.api.path.IPathModifier;
import com.monolithapi.networking.SpawnEffectPayload;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class ClientEffectHandler {

    public static void handle(SpawnEffectPayload payload) {
        String id = payload.effectId();
        NbtCompound nbt = payload.data();

        Vec3d pos = new Vec3d(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"));
        Vector3f color = new Vector3f(nbt.getFloat("r"), nbt.getFloat("g"), nbt.getFloat("b"));
        int lifetime = nbt.getInt("lifetime");

        switch (id) {
            case "line" -> ParticleLine.spawnSizedParticleLine(
                    pos, getVec3(nbt, "end"), nbt.getInt("count"),
                    nbt.getFloat("minSize"), nbt.getFloat("maxSize"), color, lifetime
            );
            case "polygon" -> ParticlePolygons.spawnPolygon(
                    pos, nbt.getDouble("radius"), nbt.getInt("sides"),
                    nbt.getDouble("pitch"), nbt.getDouble("yaw"), nbt.getInt("perEdge"),
                    nbt.getFloat("minSize"), nbt.getFloat("maxSize"), color, lifetime
            );
            case "star" -> ParticlePolygons.spawnStar(
                    pos, nbt.getDouble("outerRadius"), nbt.getDouble("innerRadius"), nbt.getInt("points"),
                    nbt.getDouble("pitch"), nbt.getDouble("yaw"), nbt.getInt("perEdge"),
                    nbt.getFloat("minSize"), nbt.getFloat("maxSize"), color, lifetime
            );
            case "cube" -> Particle3DShapes.spawnCube(
                    pos, nbt.getDouble("size"), nbt.getInt("perEdge"),
                    nbt.getFloat("minSize"), nbt.getFloat("maxSize"), color, lifetime
            );
            case "pyramid" -> Particle3DShapes.spawnPyramid(
                    pos, nbt.getDouble("baseSize"), nbt.getDouble("height"), nbt.getInt("perEdge"),
                    nbt.getFloat("minSize"), nbt.getFloat("maxSize"), color, lifetime
            );
            case "sphere" -> ParticleShapes.spawnSphere(
                    pos, nbt.getDouble("radius"), nbt.getInt("count"), nbt.getFloat("size"), color, lifetime
            );
            case "orbit_ring" -> ParticleShapes.spawnOrbitRing(
                    pos, nbt.getDouble("radius"), nbt.getDouble("speed"), nbt.getInt("count"), nbt.getFloat("size"), color, lifetime
            );
            case "shockwave" -> ParticleShapes.spawnShockwave(
                    pos, nbt.getDouble("maxRadius"), nbt.getDouble("speed"), nbt.getInt("count"), nbt.getFloat("size"), color, lifetime
            );
            case "double_helix" -> ParticleEffects.spawnDoubleHelix(
                    pos, nbt.getDouble("radius"), nbt.getDouble("rotSpeed"), nbt.getDouble("vertSpeed"),
                    nbt.getDouble("maxHeight"), nbt.getInt("perStrand"), nbt.getFloat("size"),
                    color, getSecondaryColor(nbt), lifetime
            );
            case "atom_aura" -> ParticleEffects.spawnAtomAura(
                    pos, nbt.getDouble("radius"), nbt.getDouble("speed"), nbt.getInt("perRing"), nbt.getFloat("size"), color, lifetime
            );
            case "ground_slam" -> ParticleEffects.spawnGroundSlam(pos, color, getSecondaryColor(nbt), 2.0);
            case "sine_path" -> {
                Vec3d end = getVec3(nbt, "end");
                MonolithAPI.create()
                        .at(pos)
                        .behavior(new PathBehavior(pos, end, IPathModifier.sineWave(nbt.getDouble("amplitude"), nbt.getDouble("frequency"))))
                        .color(color.x, color.y, color.z)
                        .size(nbt.getFloat("size"))
                        .lifetime(lifetime)
                        .spawn();
            }
            case "rising_zone" -> ParticleZones.spawnRisingZone(
                    pos, nbt.getDouble("radius"), nbt.getInt("spawnChance"),
                    nbt.getDouble("minHeight"), nbt.getDouble("maxHeight"),
                    nbt.getFloat("minSize"), nbt.getFloat("maxSize"),
                    color, lifetime, nbt.getInt("maxLifetime")
            );
        }
    }

    private static Vec3d getVec3(NbtCompound nbt, String prefix) {
        return new Vec3d(nbt.getDouble(prefix + "X"), nbt.getDouble(prefix + "Y"), nbt.getDouble(prefix + "Z"));
    }

    private static Vector3f getSecondaryColor(NbtCompound nbt) {
        return new Vector3f(nbt.getFloat("r2"), nbt.getFloat("g2"), nbt.getFloat("b2"));
    }
}

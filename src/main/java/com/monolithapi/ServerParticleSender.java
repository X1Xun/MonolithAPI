package com.monolithapi;

import com.monolithapi.networking.SpawnEffectPayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class ServerParticleSender {

    private static final double DEFAULT_VIEW_DISTANCE = 64.0;

    public static void sendToNearby(ServerWorld world, Vec3d pos, String effectId, NbtCompound data) {
        data.putDouble("x", pos.x);
        data.putDouble("y", pos.y);
        data.putDouble("z", pos.z);

        SpawnEffectPayload payload = new SpawnEffectPayload(effectId, data);

        for (ServerPlayerEntity player : PlayerLookup.around(world, pos, DEFAULT_VIEW_DISTANCE)) {
            if (ServerPlayNetworking.canSend(player, SpawnEffectPayload.ID)) {
                ServerPlayNetworking.send(player, payload);
            }
        }
    }

    private static NbtCompound baseNbt(Vector3f color, int lifetime) {
        NbtCompound nbt = new NbtCompound();
        nbt.putFloat("r", color.x);
        nbt.putFloat("g", color.y);
        nbt.putFloat("b", color.z);
        nbt.putInt("lifetime", lifetime);
        return nbt;
    }

    private static void putVec3(NbtCompound nbt, String prefix, Vec3d vec) {
        nbt.putDouble(prefix + "X", vec.x);
        nbt.putDouble(prefix + "Y", vec.y);
        nbt.putDouble(prefix + "Z", vec.z);
    }

    // --- 1. ЛИНИЯ ---
    public static void spawnLine(ServerWorld world, Vec3d start, Vec3d end, int count, float minSize, float maxSize, Vector3f color, int lifetime) {
        NbtCompound nbt = baseNbt(color, lifetime);
        putVec3(nbt, "end", end);
        nbt.putInt("count", count);
        nbt.putFloat("minSize", minSize);
        nbt.putFloat("maxSize", maxSize);
        sendToNearby(world, start, "line", nbt);
    }

    // --- 2. 2D-МНОГОУГОЛЬНИКИ И ЗВЕЗДА ---
    public static void spawnPolygon(ServerWorld world, Vec3d center, double radius, int sides, double pitch, double yaw, int perEdge, float minSize, float maxSize, Vector3f color, int lifetime) {
        NbtCompound nbt = baseNbt(color, lifetime);
        nbt.putDouble("radius", radius);
        nbt.putInt("sides", sides);
        nbt.putDouble("pitch", pitch);
        nbt.putDouble("yaw", yaw);
        nbt.putInt("perEdge", perEdge);
        nbt.putFloat("minSize", minSize);
        nbt.putFloat("maxSize", maxSize);
        sendToNearby(world, center, "polygon", nbt);
    }

    public static void spawnStar(ServerWorld world, Vec3d center, double outerRadius, double innerRadius, int points, double pitch, double yaw, int perEdge, float minSize, float maxSize, Vector3f color, int lifetime) {
        NbtCompound nbt = baseNbt(color, lifetime);
        nbt.putDouble("outerRadius", outerRadius);
        nbt.putDouble("innerRadius", innerRadius);
        nbt.putInt("points", points);
        nbt.putDouble("pitch", pitch);
        nbt.putDouble("yaw", yaw);
        nbt.putInt("perEdge", perEdge);
        nbt.putFloat("minSize", minSize);
        nbt.putFloat("maxSize", maxSize);
        sendToNearby(world, center, "star", nbt);
    }

    // --- 3. 3D-ФИГУРЫ (КУБ И ПИРАМИДА) ---
    public static void spawnCube(ServerWorld world, Vec3d center, double size, int perEdge, float minSize, float maxSize, Vector3f color, int lifetime) {
        NbtCompound nbt = baseNbt(color, lifetime);
        nbt.putDouble("size", size);
        nbt.putInt("perEdge", perEdge);
        nbt.putFloat("minSize", minSize);
        nbt.putFloat("maxSize", maxSize);
        sendToNearby(world, center, "cube", nbt);
    }

    public static void spawnPyramid(ServerWorld world, Vec3d baseCenter, double baseSize, double height, int perEdge, float minSize, float maxSize, Vector3f color, int lifetime) {
        NbtCompound nbt = baseNbt(color, lifetime);
        nbt.putDouble("baseSize", baseSize);
        nbt.putDouble("height", height);
        nbt.putInt("perEdge", perEdge);
        nbt.putFloat("minSize", minSize);
        nbt.putFloat("maxSize", maxSize);
        sendToNearby(world, baseCenter, "pyramid", nbt);
    }

    // --- 4. СФЕРА, ОРБИТА И УДАРНАЯ ВОЛНА ---
    public static void spawnSphere(ServerWorld world, Vec3d center, double radius, int count, float size, Vector3f color, int lifetime) {
        NbtCompound nbt = baseNbt(color, lifetime);
        nbt.putDouble("radius", radius);
        nbt.putInt("count", count);
        nbt.putFloat("size", size);
        sendToNearby(world, center, "sphere", nbt);
    }

    public static void spawnOrbitRing(ServerWorld world, Vec3d center, double radius, double speed, int count, float size, Vector3f color, int lifetime) {
        NbtCompound nbt = baseNbt(color, lifetime);
        nbt.putDouble("radius", radius);
        nbt.putDouble("speed", speed);
        nbt.putInt("count", count);
        nbt.putFloat("size", size);
        sendToNearby(world, center, "orbit_ring", nbt);
    }

    public static void spawnShockwave(ServerWorld world, Vec3d center, double maxRadius, double speed, int count, float size, Vector3f color, int lifetime) {
        NbtCompound nbt = baseNbt(color, lifetime);
        nbt.putDouble("maxRadius", maxRadius);
        nbt.putDouble("speed", speed);
        nbt.putInt("count", count);
        nbt.putFloat("size", size);
        sendToNearby(world, center, "shockwave", nbt);
    }

    // --- 5. СОСТАВНЫЕ ЭФФЕКТЫ ---
    public static void spawnDoubleHelix(ServerWorld world, Vec3d baseCenter, double radius, double rotSpeed, double vertSpeed, double maxHeight, int perStrand, float size, Vector3f primary, Vector3f secondary, int lifetime) {
        NbtCompound nbt = baseNbt(primary, lifetime);
        nbt.putDouble("radius", radius);
        nbt.putDouble("rotSpeed", rotSpeed);
        nbt.putDouble("vertSpeed", vertSpeed);
        nbt.putDouble("maxHeight", maxHeight);
        nbt.putInt("perStrand", perStrand);
        nbt.putFloat("size", size);
        nbt.putFloat("r2", secondary.x);
        nbt.putFloat("g2", secondary.y);
        nbt.putFloat("b2", secondary.z);
        sendToNearby(world, baseCenter, "double_helix", nbt);
    }

    public static void spawnAtomAura(ServerWorld world, Vec3d center, double radius, double speed, int perRing, float size, Vector3f color, int lifetime) {
        NbtCompound nbt = baseNbt(color, lifetime);
        nbt.putDouble("radius", radius);
        nbt.putDouble("speed", speed);
        nbt.putInt("perRing", perRing);
        nbt.putFloat("size", size);
        sendToNearby(world, center, "atom_aura", nbt);
    }

    public static void spawnGroundSlam(ServerWorld world, Vec3d impactPos, Vector3f mainColor, Vector3f accentColor) {
        NbtCompound nbt = baseNbt(mainColor, 40);
        nbt.putFloat("r2", accentColor.x);
        nbt.putFloat("g2", accentColor.y);
        nbt.putFloat("b2", accentColor.z);
        sendToNearby(world, impactPos, "ground_slam", nbt);
    }

    // --- 6. ДВИЖЕНИЕ ИЗ А В Б ПО СИНУСОИДЕ И ЗОНА ПОДЪЕМА ---
    public static void spawnSineWaveParticle(ServerWorld world, Vec3d start, Vec3d end, double amplitude, double frequency, float size, Vector3f color, int lifetime) {
        NbtCompound nbt = baseNbt(color, lifetime);
        putVec3(nbt, "end", end);
        nbt.putDouble("amplitude", amplitude);
        nbt.putDouble("frequency", frequency);
        nbt.putFloat("size", size);
        sendToNearby(world, start, "sine_path", nbt);
    }

    public static void spawnRisingZone(ServerWorld world, Vec3d groundCenter, double radius, float spawnChance, double minHeight, double maxHeight, float minSize, float maxSize, Vector3f color, int minLifetime, int maxLifetime) {
        NbtCompound nbt = baseNbt(color, minLifetime);
        nbt.putDouble("radius", radius);
        nbt.putFloat("spawnChance", spawnChance);
        nbt.putDouble("minHeight", minHeight);
        nbt.putDouble("maxHeight", maxHeight);
        nbt.putFloat("minSize", minSize);
        nbt.putFloat("maxSize", maxSize);
        nbt.putInt("maxLifetime", maxLifetime);
        sendToNearby(world, groundCenter, "rising_zone", nbt);
    }
}

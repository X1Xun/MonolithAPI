package com.monolithapi.networking;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SpawnEffectPayload(String effectId, NbtCompound data) implements CustomPayload {
    public static final CustomPayload.Id<SpawnEffectPayload> ID =
            new CustomPayload.Id<>(Identifier.of("monolithapi", "spawn_effect"));

    public static final PacketCodec <RegistryByteBuf, SpawnEffectPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, SpawnEffectPayload::effectId,
            PacketCodecs.NBT_COMPOUND, SpawnEffectPayload::data,
            SpawnEffectPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

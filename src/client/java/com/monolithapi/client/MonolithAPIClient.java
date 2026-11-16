package com.monolithapi.client;

import com.monolithapi.api.methods.Particle3DShapes;
import com.monolithapi.api.methods.ParticleEffects;
import com.monolithapi.api.methods.ParticleShapes;
import com.monolithapi.networking.SpawnEffectPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;


public class MonolithAPIClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.world != null && !client.isPaused()) {
				ParticleManager.INSTANCE.tick();
			}
		});
		ClientPlayNetworking.registerGlobalReceiver(SpawnEffectPayload.ID, (payload, context) -> {
			context.client().execute(() -> ClientEffectHandler.handle(payload));
		});
	}
}
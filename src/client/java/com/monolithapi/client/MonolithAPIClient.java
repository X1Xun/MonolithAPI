package com.monolithapi.client;

import com.monolithapi.api.ParticleAPI;
import com.monolithapi.api.behaviors.PathBehavior;
import com.monolithapi.api.methods.Particle3DShapes;
import com.monolithapi.api.methods.ParticleEffects;
import com.monolithapi.api.path.IPathModifier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class MonolithAPIClient implements ClientModInitializer {
	private static KeyBinding spawnEffectKey;
	@Override
	public void onInitializeClient() {
		spawnEffectKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.effects.spawn_test",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_R,
				"category.effects.general"
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.world != null && !client.isPaused()) {
				ParticleManager.INSTANCE.tick();
				Particle3DShapes.spawnCube(new Vec3d(0, 100, 0), 5.0F, 10, 1, 1, new Vector3f(0.0F, 1.0F, 0.0F), 30);
			}
			if (spawnEffectKey.isPressed())	 {
				if (client.player != null) {
					var player = client.player;
					Vec3d spawnPos = new Vec3d(player.getX(), player.getY()+0.1F, player.getZ());
//					ParticleEffects.spawnGroundSlam(
//							spawnPos,
//							new Vector3f((float) 93/255, (float) 179/255, (float) 232/255),
//							new Vector3f((float) 93/255, (float) 179/255, (float) 232/255),
//							3.5F
//					);
					Vec3d start = client.player.getEyePos();
					Vec3d end = start.add(client.player.getRotationVec(1.0f).multiply(12.0));
//					ParticleAPI.create()
//							.at(start)
//							.behavior(new PathBehavior(start, end, IPathModifier.spiral(0.6, 3.0, 0.0)))
//							.color(0.2f, 0.9f, 1.0f)
//							.size(0.25f)
//							.lifetime(80)
//							.spawn();
//					ParticleAPI.create()
//							.at(start)
//							.behavior(new PathBehavior(start, end, (progress, dist) -> {
//								double arcY = Math.sin(Math.PI * progress) * 2.0; // Дуга вверх на 2 блока
//								double jitterX = Math.cos(progress * 25.0) * 0.15; // Вибрация влево-вправо
//								return new Vec3d(jitterX, arcY, 0.0);
//							}))
//							.color(1.0f, 0.4f, 0.9f)
//							.size(0.25f)
//							.lifetime(30)
//							.spawn();

				}
			}
		});
	}
}
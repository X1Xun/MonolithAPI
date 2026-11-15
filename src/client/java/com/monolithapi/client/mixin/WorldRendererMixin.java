package com.monolithapi.client.mixin;

import com.monolithapi.client.ParticleManager;
import com.monolithapi.client.ThreedParticle;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;renderWorldBorder(Lnet/minecraft/client/render/Camera;)V"
            )
    )
    private void renderCustom3DParticles(RenderTickCounter tickCounter, boolean renderBlockOutline,
                                         Camera camera, GameRenderer gameRenderer,
                                         LightmapTextureManager lightmapTextureManager,
                                         Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {

        // Тестовый спавн (потом лучше вынести из рендера в клиентский тик)
//        if (ParticleManager.INSTANCE.getParticles().isEmpty()) {
//            ParticleAPI.create()
//                    .at(new Vec3d(0, 100, 0))
//                    .behavior(new OrbitBehavior(1.3f, 0.1f))
//                    .color(1.0f, 0.0f, 0.0f)
//                    .size(0.3f)
//                    .lifetime(12000)
//                    .spawn();
//        }

        // Если частиц всё равно нет — выходим до инициализации OpenGL и BufferBuilder
        if (ParticleManager.INSTANCE.getParticles().isEmpty()) {
            return;
        }

        float tickDelta = tickCounter.getTickDelta(true);
        Vec3d cameraPos = camera.getPos();
        Quaternionf cameraRotation = camera.getRotation();
        MatrixStack matrices = new MatrixStack();

        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        synchronized (ParticleManager.INSTANCE.getParticles()) {
            for (ThreedParticle particle : ParticleManager.INSTANCE.getParticles()) {
                // Обновляем size, color и alpha перед отрисовкой кадра
                particle.prepareRender(tickDelta);

                // Если партикл полностью прозрачный, пропускаем отрисовку
                if (particle.alpha <= 0.001f) continue;

                // 1. Отрисовка шлейфа (через for-each, так как getTrailPositions возвращает Collection/Deque)
                Collection<Vec3d> trail = particle.getTrailPositions();
                int trailSizeCount = trail.size();
                int index = 0;

                for (Vec3d trailPos : trail) {
                    matrices.push();
                    matrices.translate(trailPos.x - cameraPos.x, trailPos.y - cameraPos.y, trailPos.z - cameraPos.z);
                    matrices.multiply(cameraRotation);

                    float progress = (float) index / trailSizeCount;
                    float trailRadius = particle.size * (1.0f - progress * 0.7f);
                    // Умножаем прозрачность шлейфа на общую alpha партикла!
                    float trailAlpha = (0.5f * (1.0f - progress)) * particle.alpha;

                    drawGlowCircle(bufferBuilder, matrices.peek().getPositionMatrix(), trailRadius, particle.color, trailAlpha);

                    matrices.pop();
                    index++;
                }

                // 2. Отрисовка головы (основного ядра) партикла
                matrices.push();
                Vec3d smoothPos = particle.getInterpolatedPosition(tickDelta);
                matrices.translate(smoothPos.x - cameraPos.x, smoothPos.y - cameraPos.y, smoothPos.z - cameraPos.z);
                matrices.multiply(cameraRotation);

                float headAlpha = 0.8f * particle.alpha;
                drawGlowCircle(bufferBuilder, matrices.peek().getPositionMatrix(), particle.size * 1.3f, particle.color, headAlpha);

                matrices.pop();
            }
        }

        // Безопасное завершение буфера в 1.21.1 (защита от краша при 0 вершин)
        BuiltBuffer builtBuffer = bufferBuilder.endNullable();
        if (builtBuffer != null) {
            BufferRenderer.drawWithGlobalProgram(builtBuffer);
        }

        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
    }

    private void drawGlowCircle(BufferBuilder buffer, Matrix4f matrix, float radius, Vector3f color, float maxAlpha) {
        int segments = 8;
        float r = color.x;
        float g = color.y;
        float b = color.z;

        for (int i = 0; i < segments; i++) {
            double angle1 = (i * 2 * Math.PI) / segments;
            double angle2 = ((i + 1) * 2 * Math.PI) / segments;

            float x1 = (float) (radius * Math.cos(angle1));
            float y1 = (float) (radius * Math.sin(angle1));
            float x2 = (float) (radius * Math.cos(angle2));
            float y2 = (float) (radius * Math.sin(angle2));

            buffer.vertex(matrix, 0.0f, 0.0f, 0.0f).color(r, g, b, maxAlpha);
            buffer.vertex(matrix, x1, y1, 0.0f).color(r, g, b, 0.0f);
            buffer.vertex(matrix, x2, y2, 0.0f).color(r, g, b, 0.0f);
            buffer.vertex(matrix, 0.0f, 0.0f, 0.0f).color(r, g, b, maxAlpha);
        }
    }
}

package me.bakje.bakjedev.bakjedev.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.event.events.*;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.joml.Matrix4f;
import net.minecraft.util.profiler.Profiler;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow
    private void drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState) {}

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"))
    private void render_swap(Profiler profiler, String string) {
        profiler.swap(string);

        if (string.equals("entities")) {
            Bakjedev.INSTANCE.eventBus.post(new EntityRenderEvent());
        } else if (string.equals("blockentities")) {
            Bakjedev.INSTANCE.eventBus.post(new EntityRenderEvent.PostAll());
            Bakjedev.INSTANCE.eventBus.post(new BlockEntityRenderEvent());
        } else if (string.equals("destroyProgress")) {
            Bakjedev.INSTANCE.eventBus.post(new BlockEntityRenderEvent.PostAll());
        }
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render_head(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
                             LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo callback) {
        WorldRenderEvent.Pre event = new WorldRenderEvent.Pre(tickDelta, matrices);
        Bakjedev.INSTANCE.eventBus.post(event);

        if (event.isCancelled()) {
            callback.cancel();
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void render_return(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
                               LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo callback) {
        RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);
        WorldRenderEvent.Post event = new WorldRenderEvent.Post(tickDelta, matrices);
        Bakjedev.INSTANCE.eventBus.post(event);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;DDDLnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    private void render_drawBlockOutline(WorldRenderer worldRenderer, MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState) {
        RenderBlockOutlineEvent event = new RenderBlockOutlineEvent(matrices, vertexConsumer, blockPos, blockState);
        Bakjedev.INSTANCE.eventBus.post(event);

        if (!event.isCancelled()) {
            drawBlockOutline(event.getMatrices(), event.getVertexConsumer(), entity, d, e, f, event.getPos(), event.getState());
        }
    }

    @Redirect(method = "renderEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;render(Lnet/minecraft/entity/Entity;DDDFFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private <E extends Entity> void renderEntity_render(EntityRenderDispatcher dispatcher, E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        EntityRenderEvent.Single.Pre event = new EntityRenderEvent.Single.Pre(entity, matrices, vertexConsumers);
        Bakjedev.INSTANCE.eventBus.post(event);

        if (!event.isCancelled()) {
            dispatcher.render(event.getEntity(), x, y, z, yaw, tickDelta, event.getMatrix(), event.getVertex(), light);
        }
    }

    @Redirect(method = "renderEndSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;color(IIII)Lnet/minecraft/client/render/VertexConsumer;"))
    private VertexConsumer renderEndSky_color(VertexConsumer vertexConsumer, int red, int green, int blue, int alpha) {
        SkyRenderEvent.Color.EndSkyColor event = new SkyRenderEvent.Color.EndSkyColor(1f);
        Bakjedev.INSTANCE.eventBus.post(event);

        if (event.getColor() != null) {
            return vertexConsumer.color(
                    (int) (event.getColor().x * 255), (int) (event.getColor().y * 255), (int) (event.getColor().z * 255), (int) alpha);
        } else {
            return vertexConsumer.color(red, green, blue, alpha);
        }
    }
}

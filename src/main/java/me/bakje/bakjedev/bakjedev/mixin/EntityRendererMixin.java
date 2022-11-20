package me.bakje.bakjedev.bakjedev.mixin;

import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.event.events.EntityRenderEvent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity>  {


    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    private void renderLabelIfPresent(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        EntityRenderEvent.Single.Label event = new EntityRenderEvent.Single.Label(entity, matrices, vertexConsumers);
        Bakjedev.INSTANCE.eventBus.post(event);

        if (event.isCancelled()) {
            info.cancel();
        }
    }


}

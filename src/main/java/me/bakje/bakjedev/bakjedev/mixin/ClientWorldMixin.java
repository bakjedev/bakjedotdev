package me.bakje.bakjedev.bakjedev.mixin;

import me.bakje.bakjedev.bakjedev.util.bakjeQueue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.world.ClientWorld;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void tickEntities(CallbackInfo info) {
        bakjeQueue.nextQueue();
    }
}

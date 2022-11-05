package me.bakje.bakjedev.bakjedev.mixin;


import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.entity.LivingEntity.class)
public class LivingEntityMixin {
    // Disable regular movement when WorldGuard Bypass is enabled
    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isImmobile()Z"))
    private boolean isImmobile(net.minecraft.entity.LivingEntity livingEntity) {
        if (ModuleManager.INSTANCE.isModEnabled("WGbypass").isEnabled()) {
            return true;
        }
        return false;

    }

    @Inject(method = "tickStatusEffects", at = @At(value = "INVOKE", ordinal = 0,
            target = "Lnet/minecraft/entity/data/DataTracker;get(Lnet/minecraft/entity/data/TrackedData;)Ljava/lang/Object;"),
            cancellable = true)
    private void removeOwnPotionEffects(CallbackInfo ci)
    {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (ModuleManager.INSTANCE.isModEnabled("CleanView").isEnabled() &&
                ((Object) this) == mc.player && mc.options.getPerspective() == Perspective.FIRST_PERSON)
        {
            ci.cancel();
        }
    }
}
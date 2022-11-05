package me.bakje.bakjedev.bakjedev.mixin;

import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin  extends LivingEntity {
    @Shadow protected abstract boolean clipAtLedge();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "adjustMovementForSneaking", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;clipAtLedge()Z", ordinal = 0))
    private boolean fakeSneaking(PlayerEntity entity)
    {
        if (ModuleManager.INSTANCE.isModEnabled("FakeSneak").isEnabled() || ModuleManager.INSTANCE.isModEnabled("Scaffold").isEnabled() && ((Object) this) instanceof ClientPlayerEntity)
        {
            return true;
        }

        return this.clipAtLedge();
    }
}

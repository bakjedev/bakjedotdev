package me.bakje.bakjedev.bakjedev.mixin;

import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.event.events.PlayerPushedEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Entity.class)
public class EntityMixin {
    @ModifyArgs(method = "pushAwayFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    private void pushAwayFrom_addVelocity(Args args) {
        if ((Object) this == MinecraftClient.getInstance().player) {
            PlayerPushedEvent event = new PlayerPushedEvent(args.get(0), args.get(1), args.get(2));
            Bakjedev.INSTANCE.eventBus.post(event);

            args.set(0, event.getPushX());
            args.set(1, event.getPushY());
            args.set(2, event.getPushZ());
        }
    }
}

package me.bakje.bakjedev.bakjedev.mixin;

import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.event.events.EntityControlEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AbstractHorseEntity.class, PigEntity.class, StriderEntity.class})
public abstract class LlamaPigStriderEntityMixin extends AnimalEntity {

    private LlamaPigStriderEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "isSaddled", at = @At("HEAD"), cancellable = true)
    private void isSaddled(CallbackInfoReturnable<Boolean> info) {
        EntityControlEvent event = new EntityControlEvent();
        Bakjedev.INSTANCE.eventBus.post(event);

        if (event.canBeControlled() != null) {
            info.setReturnValue(event.canBeControlled());
        }
    }
}

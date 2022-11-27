package me.bakje.bakjedev.bakjedev.mixin;

import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.render.Fullbright;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SimpleOption.DoubleSliderCallbacks.class)
public class SimpleOptionMixin {
    @Inject(method = "validate(Ljava/lang/Double;)Ljava/util/Optional;", at = @At("RETURN"), cancellable = true)
    public void removeValidation(Double value, CallbackInfoReturnable<Optional<Double>> cir) {
        if (ModuleManager.INSTANCE.getModule(Fullbright.class).isEnabled()) {
            if (value == 100.0) {
                cir.setReturnValue(Optional.of(100.0));
            }
        }
    }
}

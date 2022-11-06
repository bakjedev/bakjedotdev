package me.bakje.bakjedev.bakjedev.mixin;

import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.Render.NoFog;
import net.minecraft.client.render.DimensionEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionEffects.Nether.class)
public abstract class DimensionEffectsMixin extends DimensionEffects{
    public DimensionEffectsMixin(float cloudsHeight, boolean alternateSkyColor, SkyType skyType, boolean brightenLighting, boolean darkened) {
        super(cloudsHeight, alternateSkyColor, skyType, brightenLighting, darkened);
    }

    @Inject(method = "useThickFog", at = @At("HEAD"), cancellable = true)
    private void disableNetherFog(int x, int z, CallbackInfoReturnable<Boolean> cir)
    {
//        if (ModuleManager.INSTANCE.isModEnabled("NoFog").isEnabled())
        if (ModuleManager.INSTANCE.getModule(NoFog.class).isEnabled())
        {
            cir.setReturnValue(false);
        }
    }
}

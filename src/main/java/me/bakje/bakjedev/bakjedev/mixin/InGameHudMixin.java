package me.bakje.bakjedev.bakjedev.mixin;

import me.bakje.bakjedev.bakjedev.UI.Hud;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.Render.NoVanillaEffectHUD;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("RETURN"))
    public void renderHud(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        Hud.Render(matrices, tickDelta);
    }

    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
    private void disableStatusEffectHUD(CallbackInfo ci) {
//        if (ModuleManager.INSTANCE.isModEnabled("NoEffectHud").isEnabled()) {
        if (ModuleManager.INSTANCE.getModule(NoVanillaEffectHUD.class).isEnabled()) {
            ci.cancel();
        }
    }
}

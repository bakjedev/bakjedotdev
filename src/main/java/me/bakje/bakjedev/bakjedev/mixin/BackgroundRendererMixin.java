package me.bakje.bakjedev.bakjedev.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.Render.NoRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
    @Inject(method = "applyFog",
            require = 0,
            at = @At(value = "INVOKE", remap = false,
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V",
                    shift = At.Shift.AFTER))
    private static void disableRenderDistanceFog(
            Camera camera,
            BackgroundRenderer.FogType fogType,
            float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci)
    {
        if (ModuleManager.INSTANCE.getModule(NoRender.class).isEnabled() && ModuleManager.INSTANCE.getModule(NoRender.class).fog.isEnabled())
        {
            if (!thickFog)
            {
                float distance = Math.max(512, MinecraftClient.getInstance().gameRenderer.getViewDistance());
                RenderSystem.setShaderFogStart(distance * 1.6F);
                RenderSystem.setShaderFogEnd(distance * 2.0F);
            }
        }
    }
}

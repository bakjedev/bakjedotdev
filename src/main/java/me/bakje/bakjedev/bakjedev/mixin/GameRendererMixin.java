package me.bakje.bakjedev.bakjedev.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.event.events.RenderShaderEvent;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.Render.NoRender;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow private ShaderEffect shader;
    @Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
    public void disableHurtCam(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModule(NoRender.class).isEnabled() && ModuleManager.INSTANCE.getModule(NoRender.class).hurtcam.isEnabled()) ci.cancel();
    }

    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;shader:Lnet/minecraft/client/gl/ShaderEffect;", ordinal = 0))
    private ShaderEffect render_Shader(GameRenderer renderer, float tickDelta) {
        RenderShaderEvent event = new RenderShaderEvent(shader);
        Bakjedev.INSTANCE.eventBus.post(event);

        if (event.getEffect() != null) {
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.enableTexture();
            RenderSystem.resetTextureMatrix();
            event.getEffect().render(tickDelta);
        }

        return null;
    }
}
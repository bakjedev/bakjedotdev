package me.bakje.bakjedev.bakjedev.mixin;

import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.render.ViewClip;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {
    @Unique private boolean bypassCameraClip;

    @Shadow private double clipToSpace(double desiredCameraDistance) {return 0;}

    @Inject(method = "clipToSpace", at = @At("HEAD"), cancellable = true)
    private void onClipToSpace(double desiredCameraDistance, CallbackInfoReturnable<Double> info) {
        if (bypassCameraClip) {
            bypassCameraClip = false;
        } else {
            if (ModuleManager.INSTANCE.getModule(ViewClip.class).isEnabled()) {
                if (ModuleManager.INSTANCE.getModule(ViewClip.class).CameraClip.isEnabled()) {
                    info.setReturnValue(ModuleManager.INSTANCE.getModule(ViewClip.class).DistanceToggle.isEnabled()
                            ? ModuleManager.INSTANCE.getModule(ViewClip.class).Distance.getValue() : desiredCameraDistance);
                } else if (ModuleManager.INSTANCE.getModule(ViewClip.class).DistanceToggle.isEnabled()) {
                    bypassCameraClip = true;
                    info.setReturnValue(clipToSpace(ModuleManager.INSTANCE.getModule(ViewClip.class).Distance.getValue()));
                }
            }
        }
    }
}


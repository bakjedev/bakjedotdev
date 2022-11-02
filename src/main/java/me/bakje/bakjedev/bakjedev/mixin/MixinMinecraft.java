package me.bakje.bakjedev.bakjedev.mixin;


import me.bakje.bakjedev.bakjedev.Bakjedev;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MinecraftClient.class)
public class MixinMinecraft {


    @Inject(at=@At("HEAD"), method="tick")
    public void onTick(CallbackInfo ci) {
        Bakjedev.INSTANCE.onTick();
    }
}

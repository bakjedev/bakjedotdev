package me.bakje.bakjedev.bakjedev.mixin;


import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MinecraftClient.class)
public class MixinMinecraft {
    @Shadow
    @Nullable public ClientPlayerEntity player;
    @Shadow @Nullable public Screen currentScreen;
    @Shadow @Final public GameOptions options;
    @Shadow protected int attackCooldown;
    @Inject(at=@At("HEAD"), method="tick")
    public void onTick(CallbackInfo ci) {
        Bakjedev.INSTANCE.onTick();
    }

    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void onProcessKeybindsPre(CallbackInfo ci) {
        if (this.currentScreen == null)
        {
            if (ModuleManager.INSTANCE.isModEnabled("HoldAttack").isEnabled())
            {
                if (this.attackCooldown >= 10000)
                {
                    this.attackCooldown = 0;
                }

                KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(this.options.attackKey.getBoundKeyTranslationKey()), true);
            }

            if (ModuleManager.INSTANCE.isModEnabled("HoldUse").isEnabled())
            {
                KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(this.options.useKey.getBoundKeyTranslationKey()), true);
            }
        }
    }
}

package me.bakje.bakjedev.bakjedev.mixin;


import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.event.events.OpenScreenEvent;
import me.bakje.bakjedev.bakjedev.module.Misc.HoldAction;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
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
public abstract class MinecraftClientMixin {
    @Shadow
    @Nullable
    public ClientPlayerEntity player;
    @Shadow
    @Nullable
    public Screen currentScreen;
    @Shadow
    @Final
    public GameOptions options;
    @Shadow
    protected int attackCooldown;
    //onTick
    @Inject(at = @At("HEAD"), method = "tick")
    public void onTick(CallbackInfo ci) {
        Bakjedev.INSTANCE.onTick();
    }


    //HoldAction
    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void onProcessKeybindsPre(CallbackInfo ci) {
        if (this.currentScreen == null) {
            if (ModuleManager.INSTANCE.getModule(HoldAction.class).isEnabled()) {
                if(ModuleManager.INSTANCE.getModule(HoldAction.class).actionMode.isMode("Attack")) {
                    if (this.attackCooldown >= 10000) {
                        this.attackCooldown = 0;
                    }

                    KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(this.options.attackKey.getBoundKeyTranslationKey()), true);
                } else {
                    KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(this.options.useKey.getBoundKeyTranslationKey()), true);
                }
            }
        }
    }


    //Set Screen
    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    public void setScreen(Screen screen, CallbackInfo ci) {
        OpenScreenEvent event = new OpenScreenEvent(screen);
        Bakjedev.INSTANCE.eventBus.post(event);

        if  (event.isCancelled()) {
            ci.cancel();
        }
    }

}


package me.bakje.bakjedev.bakjedev.mixin;


import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.event.events.OpenScreenEvent;
import me.bakje.bakjedev.bakjedev.module.misc.HoldAction;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.util.bedrockUtil.BreakingFlowController;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    @Nullable
    public ClientWorld world;
    @Shadow
    @Nullable
    public HitResult crosshairTarget;
    private Window window;
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
                if(ModuleManager.INSTANCE.getModule(HoldAction.class).actionMode.getIndex()==1) {
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


    @Inject(method = "handleBlockBreaking", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void inject(boolean bl, CallbackInfo ci, BlockHitResult blockHitResult, BlockPos blockPos, Direction direction) {
        if (world.getBlockState(blockPos).isOf(Blocks.BEDROCK) && BreakingFlowController.isWorking()) {
            BreakingFlowController.addBlockPosToList(blockPos);
        }


    }

}


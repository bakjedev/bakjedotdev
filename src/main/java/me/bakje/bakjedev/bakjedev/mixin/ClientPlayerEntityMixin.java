package me.bakje.bakjedev.bakjedev.mixin;

import me.bakje.bakjedev.bakjedev.UI.Hud;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)

public class ClientPlayerEntityMixin {
    MinecraftClient mc = MinecraftClient.getInstance();
    Screen tempCurrentScreen;
    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;", opcode = Opcodes.GETFIELD, ordinal = 0), method = {"updateNausea()V"})
    public void beforeUpdateNausea(CallbackInfo ci) {
        if (ModuleManager.INSTANCE.isModEnabled("PortalGUI").isEnabled()) {
            tempCurrentScreen = mc.currentScreen;
            mc.currentScreen = null;
        }
    }
    @Inject(at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;nextNauseaStrength:F",
            opcode = Opcodes.GETFIELD,
            ordinal = 1), method = {"updateNausea()V"})
    private void afterUpdateNausea(CallbackInfo ci)
    {
        if(tempCurrentScreen == null)
            return;

        mc.currentScreen = tempCurrentScreen;
        tempCurrentScreen = null;
    }
}

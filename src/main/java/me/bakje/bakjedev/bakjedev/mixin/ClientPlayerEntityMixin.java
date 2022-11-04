package me.bakje.bakjedev.bakjedev.mixin;

import me.bakje.bakjedev.bakjedev.UI.Hud;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
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

    @Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
    public void onChatMessage(String message, Text preview, CallbackInfo ci) {
        if (message.startsWith("$")
        && !message.contains("help")
        && !message.contains("vclip")
        && !message.contains("hclip")) {
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            mc.player.sendMessage(prefixString.append(Text.literal("Unknown or incomplete command, try $help for the list of commands.").formatted(Formatting.GRAY)), false);
            ci.cancel();
        }
        if (message.equalsIgnoreCase("$help")) {
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            mc.player.sendMessage(prefixString.append(Text.literal("List of all commands:").formatted(Formatting.GRAY)), false);
            mc.player.sendMessage(Text.literal("    - help: gives list of commands").formatted(Formatting.GRAY), false);
            mc.player.sendMessage(Text.literal("    - vclip: teleports you up, pass in number with max 3 digits").formatted(Formatting.GRAY), false);
            ci.cancel();
        }
        int number;
        if (message.startsWith("$vclip")) {
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            try {
                if (message.length() == 8) {
                    number = Integer.parseInt(message.substring(message.length() - 1));
                } else if (message.length() == 9) {
                    number = Integer.parseInt(message.substring(message.length() - 2));
                } else {
                    number = Integer.parseInt(message.substring(message.length() - 3));
                }
                mc.player.updatePosition(mc.player.getX(), mc.player.getY()+number, mc.player.getZ());
                mc.player.sendMessage(prefixString.append(Text.literal("Teleported").formatted(Formatting.GRAY)), false);
            }
            catch (Exception e) {
                mc.player.sendMessage(prefixString.append(Text.literal("Invalid argument").formatted(Formatting.GRAY)), false);
            }
            ci.cancel();
        }
        int number2;
        if (message.startsWith("$hclip")) {
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            try {
                if (message.length() == 8) {
                    number2 = Integer.parseInt(message.substring(message.length() - 1));
                } else if (message.length() == 9) {
                    number2 = Integer.parseInt(message.substring(message.length() - 2));
                } else {
                    number2 = Integer.parseInt(message.substring(message.length() - 3));
                }
                if (mc.player.getMovementDirection() == Direction.SOUTH){
                    mc.player.updatePosition(mc.player.getX(), mc.player.getY(), mc.player.getZ()+number2);
                } else if (mc.player.getMovementDirection() == Direction.WEST) {
                    mc.player.updatePosition(mc.player.getX()-number2, mc.player.getY(), mc.player.getZ());
                } else if (mc.player.getMovementDirection() == Direction.NORTH) {
                    mc.player.updatePosition(mc.player.getX(), mc.player.getY(), mc.player.getZ()-number2);
                } else  if (mc.player.getMovementDirection() == Direction.EAST){
                    mc.player.updatePosition(mc.player.getX()+number2, mc.player.getY(), mc.player.getZ());
                }
                mc.player.sendMessage(prefixString.append(Text.literal("Teleported").formatted(Formatting.GRAY)), false);
            }
            catch (Exception e) {
                mc.player.sendMessage(prefixString.append(Text.literal("Invalid argument").formatted(Formatting.GRAY)), false);
            }
            ci.cancel();
        }
    }
}


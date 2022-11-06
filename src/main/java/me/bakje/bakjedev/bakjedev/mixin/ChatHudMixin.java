package me.bakje.bakjedev.bakjedev.mixin;

import me.bakje.bakjedev.bakjedev.module.Misc.ChatTimestamps;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


import java.text.SimpleDateFormat;
import java.util.Date;

@Mixin(value = ChatHud.class, priority = 1100)
public abstract class ChatHudMixin extends net.minecraft.client.gui.DrawableHelper {
    private static final Date DATE = new Date();

    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", at = @At("HEAD"), argsOnly = true)
    private Text addMessageTimestamp(Text componentIn) {

        if (ModuleManager.INSTANCE.getModule(ChatTimestamps.class).isEnabled()) {

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            DATE.setTime(System.currentTimeMillis());

            MutableText newComponent = Text.literal("["+sdf.format(DATE) + "] ");

            newComponent.append(componentIn);
            return newComponent;
        }
        return componentIn;
    }
}
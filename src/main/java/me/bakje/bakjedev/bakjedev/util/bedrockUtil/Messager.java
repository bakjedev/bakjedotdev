package me.bakje.bakjedev.bakjedev.util.bedrockUtil;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Messager {

    public static void chat(String message){
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
        minecraftClient.player.sendMessage(prefixString.append(Text.literal(message).formatted(Formatting.GRAY)), false);
    }
}

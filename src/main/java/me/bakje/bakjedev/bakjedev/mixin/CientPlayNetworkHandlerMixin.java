package me.bakje.bakjedev.bakjedev.mixin;

import me.bakje.bakjedev.bakjedev.util.ItemContentUtil;
import me.bakje.bakjedev.bakjedev.util.PeekShulkerScreen;
import me.bakje.bakjedev.bakjedev.util.bakjeQueue;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ClientPlayNetworkHandler.class)
public class CientPlayNetworkHandlerMixin {
    MinecraftClient mc = MinecraftClient.getInstance();
    @Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
    public void onChatMessage(String content, CallbackInfo ci) {
        if (content.startsWith("$")
                && !content.startsWith("$help")
                && !content.startsWith("$vclip")
                && !content.startsWith("$hclip")
                && !content.startsWith("$setyaw")
                && !content.startsWith("$peek")
                && !content.startsWith("$setpitch")) {
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            mc.player.sendMessage(prefixString.append(Text.literal("Unknown or incomplete command, try $help for the list of commands.").formatted(Formatting.GRAY)), false);
            ci.cancel();
        }

        if (content.equalsIgnoreCase("$help")) {
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            mc.player.sendMessage(prefixString.append(Text.literal("List of all the zaza in this bitch:").formatted(Formatting.GRAY)), false);
            mc.player.sendMessage(Text.literal("    - help: gives list of commands").formatted(Formatting.GRAY), false);
            mc.player.sendMessage(Text.literal("    - vclip: teleports you up, pass in number with max 3 digits").formatted(Formatting.GRAY), false);
            mc.player.sendMessage(Text.literal("    - hclip: teleports you forward, pass in number with max 3 digits").formatted(Formatting.GRAY), false);
            mc.player.sendMessage(Text.literal("    - setyaw: sets your yaw").formatted(Formatting.GRAY), false);
            mc.player.sendMessage(Text.literal("    - setpitch: sets your pitch").formatted(Formatting.GRAY), false);
            ci.cancel();
        }

        if (content.startsWith("$vclip")) {
            int value=0;
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            try {
                String[] arguments = content.split(" ");
                if (arguments.length>0) value = Integer.parseInt(arguments[1]);

                mc.player.updatePosition(mc.player.getX(), mc.player.getY()+value, mc.player.getZ());
                mc.player.sendMessage(prefixString.append(Text.literal("we tpin and shit on the vertical").formatted(Formatting.GRAY)), false);
            } catch (Exception e) {
                mc.player.sendMessage(prefixString.append(Text.literal("Invalid argument").formatted(Formatting.GRAY)), false);
            }
            ci.cancel();
        }

        if (content.startsWith("$hclip")) {
            int value=0;
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            try {
                String[] arguments = content.split(" ");
                if (arguments.length>0) value = Integer.parseInt(arguments[1]);

                if (mc.player.getMovementDirection() == Direction.SOUTH){
                    mc.player.updatePosition(mc.player.getX(), mc.player.getY(), mc.player.getZ()+value);
                } else if (mc.player.getMovementDirection() == Direction.WEST) {
                    mc.player.updatePosition(mc.player.getX()-value, mc.player.getY(), mc.player.getZ());
                } else if (mc.player.getMovementDirection() == Direction.NORTH) {
                    mc.player.updatePosition(mc.player.getX(), mc.player.getY(), mc.player.getZ()-value);
                } else  if (mc.player.getMovementDirection() == Direction.EAST){
                    mc.player.updatePosition(mc.player.getX()+value, mc.player.getY(), mc.player.getZ());
                }
                mc.player.sendMessage(prefixString.append(Text.literal("we tpin and shit on the horizontal").formatted(Formatting.GRAY)), false);
            } catch (Exception e) {
                mc.player.sendMessage(prefixString.append(Text.literal("Invalid argument").formatted(Formatting.GRAY)), false);
            }
            ci.cancel();
        }

        if (content.startsWith("$setyaw")) {
            double value=0;
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            try {
                String[] arguments = content.split(" ");
                if (arguments.length>0) value = Integer.parseInt(arguments[1]);

                mc.player.setYaw((float) value);
                mc.player.sendMessage(prefixString.append(Text.literal("you lookin at " + value + " for the yaw my silly gabagoose").formatted(Formatting.GRAY)), false);
            } catch (Exception e) {
                mc.player.sendMessage(prefixString.append(Text.literal("Invalid argument").formatted(Formatting.GRAY)), false);
            }
            ci.cancel();
        }

        if (content.startsWith("$setpitch")) {
            double value=0;
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            try {
                String[] arguments = content.split(" ");
                if (arguments.length>0) value = Integer.parseInt(arguments[1]);

                mc.player.setPitch((float) value);
                mc.player.sendMessage(prefixString.append(Text.literal("you lookin at " + value + " for the pitch my silly gabagoose").formatted(Formatting.GRAY)), false);
            } catch (Exception e) {
                mc.player.sendMessage(prefixString.append(Text.literal("Invalid argument").formatted(Formatting.GRAY)), false);
            }
            ci.cancel();
        }

        if (content.equals("$peek")) {
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            ItemStack item = mc.player.getInventory().getMainHandStack();

            if (item.getItem() instanceof BlockItem) {
                Block block = ((BlockItem) item.getItem()).getBlock();
                if (!(block instanceof ShulkerBoxBlock
                        || block instanceof ChestBlock
                        || block instanceof DispenserBlock
                        || block instanceof HopperBlock)) {
                    mc.player.sendMessage(prefixString.append(Text.literal("Bruh yo bitch ass must be holding a containter to peek").formatted(Formatting.GRAY)), false);
                    ci.cancel();
                    return;
                }
            } else if (item.getItem() != Items.BUNDLE) {
                mc.player.sendMessage(prefixString.append(Text.literal("Bruh yo bitch ass must be holding a containter to peek").formatted(Formatting.GRAY)), false);
                ci.cancel();
                return;
            }

            List<ItemStack> items = ItemContentUtil.getItemsInContainer(item);

            SimpleInventory inv = new SimpleInventory(items.toArray(new ItemStack[27]));

            mc.player.sendMessage(prefixString.append(Text.literal("ok we bout to try to open that shit for you").formatted(Formatting.GRAY)), false);
            bakjeQueue.add(() ->
                    mc.setScreen(new PeekShulkerScreen(
                            new ShulkerBoxScreenHandler(420, mc.player.getInventory(), inv),
                            mc.player.getInventory(),
                            item.getName())));


            ci.cancel();
        }
    }
}

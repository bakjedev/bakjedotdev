package me.bakje.bakjedev.bakjedev.mixin;

import com.mojang.authlib.GameProfile;
import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.event.events.ClientMoveEvent;
import me.bakje.bakjedev.bakjedev.module.misc.PortalGUI;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.movement.Freecam;
import me.bakje.bakjedev.bakjedev.module.movement.NoSlow;
import me.bakje.bakjedev.bakjedev.util.ItemContentUtil;
import me.bakje.bakjedev.bakjedev.util.PeekShulkerScreen;
import me.bakje.bakjedev.bakjedev.util.bakjeQueue;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.stat.StatHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ClientPlayerEntity.class)

public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    @Shadow
    private void autoJump(float dx, float dz) {}

    @Shadow @Final private StatHandler statHandler;
    MinecraftClient mc = MinecraftClient.getInstance();
    Screen tempCurrentScreen;

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
    }


    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void move(MovementType type, Vec3d movement, CallbackInfo info) {
        ClientMoveEvent event = new ClientMoveEvent(type, movement);
        Bakjedev.INSTANCE.eventBus.post(event);
        if (event.isCancelled()) {
            info.cancel();
        } else if (!type.equals(event.getType()) || !movement.equals(event.getVec())) {
            double double_1 = this.getX();
            double double_2 = this.getZ();
            super.move(event.getType(), event.getVec());
            this.autoJump((float) (this.getX() - double_1), (float) (this.getZ() - double_2));
            info.cancel();
        }
    }

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    private void pushOutOfBlocks(double x, double d, CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModule(Freecam.class).isEnabled()) {
            ci.cancel();
        }
    }

    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;", opcode = Opcodes.GETFIELD, ordinal = 0), method = {"updateNausea()V"})
    public void beforeUpdateNausea(CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModule(PortalGUI.class).isEnabled()) {
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



    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"), require = 0)
    private boolean tickMovement_isUsingItem(ClientPlayerEntity player) {
        if (ModuleManager.INSTANCE.getModule(NoSlow.class).isEnabled() && ModuleManager.INSTANCE.getModule(NoSlow.class).eating.isEnabled()) {
            return false;
        }
        return player.isUsingItem();
    }

    @Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
    public void onChatMessage(String message, Text preview, CallbackInfo ci) {
        if (message.startsWith("$")
        && !message.startsWith("$help")
        && !message.startsWith("$vclip")
        && !message.startsWith("$hclip")
        && !message.startsWith("$setyaw")
        && !message.startsWith("$peek")
        && !message.startsWith("$setpitch")) {
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            mc.player.sendMessage(prefixString.append(Text.literal("Unknown or incomplete command, try $help for the list of commands.").formatted(Formatting.GRAY)), false);
            ci.cancel();
        }

        if (message.equalsIgnoreCase("$help")) {
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            mc.player.sendMessage(prefixString.append(Text.literal("List of all commands:").formatted(Formatting.GRAY)), false);
            mc.player.sendMessage(Text.literal("    - help: gives list of commands").formatted(Formatting.GRAY), false);
            mc.player.sendMessage(Text.literal("    - vclip: teleports you up, pass in number with max 3 digits").formatted(Formatting.GRAY), false);
            mc.player.sendMessage(Text.literal("    - hclip: teleports you forward, pass in number with max 3 digits").formatted(Formatting.GRAY), false);
            mc.player.sendMessage(Text.literal("    - setyaw: sets your yaw").formatted(Formatting.GRAY), false);
            mc.player.sendMessage(Text.literal("    - setpitch: sets your pitch").formatted(Formatting.GRAY), false);
            ci.cancel();
        }

        if (message.startsWith("$vclip")) {
            int value=0;
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            try {
                String[] arguments = message.split(" ");
                if (arguments.length>0) value = Integer.parseInt(arguments[1]);

                mc.player.updatePosition(mc.player.getX(), mc.player.getY()+value, mc.player.getZ());
                mc.player.sendMessage(prefixString.append(Text.literal("Teleported").formatted(Formatting.GRAY)), false);
            } catch (Exception e) {
                mc.player.sendMessage(prefixString.append(Text.literal("Invalid argument").formatted(Formatting.GRAY)), false);
            }
            ci.cancel();
        }

        if (message.startsWith("$hclip")) {
            int value=0;
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            try {
                String[] arguments = message.split(" ");
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
                mc.player.sendMessage(prefixString.append(Text.literal("Teleported").formatted(Formatting.GRAY)), false);
            } catch (Exception e) {
                mc.player.sendMessage(prefixString.append(Text.literal("Invalid argument").formatted(Formatting.GRAY)), false);
            }
            ci.cancel();
        }

        if (message.startsWith("$setyaw")) {
            double value=0;
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            try {
                String[] arguments = message.split(" ");
                if (arguments.length>0) value = Integer.parseInt(arguments[1]);

                mc.player.setYaw((float) value);
                mc.player.sendMessage(prefixString.append(Text.literal("Set yaw to " + value).formatted(Formatting.GRAY)), false);
            } catch (Exception e) {
                mc.player.sendMessage(prefixString.append(Text.literal("Invalid argument").formatted(Formatting.GRAY)), false);
            }
            ci.cancel();
        }

        if (message.startsWith("$setpitch")) {
            double value=0;
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            try {
                String[] arguments = message.split(" ");
                if (arguments.length>0) value = Integer.parseInt(arguments[1]);

                mc.player.setPitch((float) value);
                mc.player.sendMessage(prefixString.append(Text.literal("Set pitch to " + value).formatted(Formatting.GRAY)), false);
            } catch (Exception e) {
                mc.player.sendMessage(prefixString.append(Text.literal("Invalid argument").formatted(Formatting.GRAY)), false);
            }
            ci.cancel();
        }

        if (message.equals("$peek")) {
            MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
            ItemStack item = mc.player.getInventory().getMainHandStack();

            if (item.getItem() instanceof BlockItem) {
                Block block = ((BlockItem) item.getItem()).getBlock();
                if (!(block instanceof ShulkerBoxBlock
                        || block instanceof ChestBlock
                        || block instanceof DispenserBlock
                        || block instanceof HopperBlock)) {
                    mc.player.sendMessage(prefixString.append(Text.literal("Must be holding a container to peek").formatted(Formatting.GRAY)), false);
                    return;
                }
            } else if (item.getItem() != Items.BUNDLE) {
                mc.player.sendMessage(prefixString.append(Text.literal("Must be holding a container to peek").formatted(Formatting.GRAY)), false);
                return;
            }

            List<ItemStack> items = ItemContentUtil.getItemsInContainer(item);

            SimpleInventory inv = new SimpleInventory(items.toArray(new ItemStack[27]));

            mc.player.sendMessage(prefixString.append(Text.literal("ok we bout to try open that shit for you").formatted(Formatting.GRAY)), false);
            bakjeQueue.add(() ->
                    mc.setScreen(new PeekShulkerScreen(
                            new ShulkerBoxScreenHandler(420, mc.player.getInventory(), inv),
                            mc.player.getInventory(),
                            item.getName())));


            ci.cancel();
        }
    }
}




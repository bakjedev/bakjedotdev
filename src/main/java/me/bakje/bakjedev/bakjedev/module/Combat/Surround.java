package me.bakje.bakjedev.bakjedev.module.Combat;

import me.bakje.bakjedev.bakjedev.module.Mod;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Surround extends Mod {
    public Surround() {
        super("Surround", "obi", Category.COMBAT, true);
    }
    @Override
    public void onEnable() {
        mc.player.updatePosition(mc.player.getBlockX()+0.5, mc.player.getBlockY(), mc.player.getBlockZ()+0.5);
    }
    @Override
    public void onTick() {
        int currentSlot = mc.player.getInventory().selectedSlot;
        for (int slot= 0; slot < 9; slot++){
            ItemStack stack = mc.player.getInventory().getStack(slot);
            if (stack.getItem() == Items.OBSIDIAN) {
                int obiSlot = slot;
                if (mc.world.getBlockState(mc.player.getBlockPos().offset(Direction.NORTH)).isOf(Blocks.AIR)) {
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(obiSlot));
                    place(mc.player.getBlockPos(), Direction.NORTH);
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(currentSlot));
                }
                if (mc.world.getBlockState(mc.player.getBlockPos().offset(Direction.EAST)).isOf(Blocks.AIR)) {
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(obiSlot));
                    place(mc.player.getBlockPos(), Direction.EAST);
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(currentSlot));
                }
                if (mc.world.getBlockState(mc.player.getBlockPos().offset(Direction.SOUTH)).isOf(Blocks.AIR)) {
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(obiSlot));
                    place(mc.player.getBlockPos(), Direction.SOUTH);
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(currentSlot));
                }
                if (mc.world.getBlockState(mc.player.getBlockPos().offset(Direction.WEST)).isOf(Blocks.AIR)) {
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(obiSlot));
                    place(mc.player.getBlockPos(), Direction.WEST);
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(currentSlot));
                }
            }
        }
        if (mc.currentScreen==null && mc.options.jumpKey.isPressed()) {
            this.toggle();
        }
        super.onTick();
    }


    public static void place(final BlockPos pos, final Direction d) {
        MinecraftClient mc = MinecraftClient.getInstance();
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(Vec3d.ofCenter((Vec3i) pos).add(Vec3d.of(d.getVector()).multiply(0.5)), d.getOpposite(), pos.offset(d), false));
    }
}
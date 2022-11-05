package me.bakje.bakjedev.bakjedev.module.World;

import me.bakje.bakjedev.bakjedev.module.Mod;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Scaffold extends Mod {

    public Scaffold() {
        super("Scaffold", "places blocks below you", Category.WORLD);
    }

    @Override
    public void onTick() {
        int currentSlot = mc.player.getInventory().selectedSlot;
        for (int slot= 0; slot < 9; slot++) {
            ItemStack stack = mc.player.getInventory().getStack(slot);
            if (stack.getItem() instanceof BlockItem) {
                int blockSlot = slot;
                Direction dir;
                if (mc.player.getMovementDirection()==Direction.NORTH) {
                    dir = Direction.NORTH;
                } else if (mc.player.getMovementDirection()==Direction.EAST) {
                    dir = Direction.EAST;
                } else if (mc.player.getMovementDirection()==Direction.SOUTH) {
                    dir = Direction.SOUTH;
                } else {
                    dir = Direction.WEST;
                }
                if (mc.world.getBlockState(mc.player.getBlockPos().offset(dir).offset(Direction.DOWN)).isOf(Blocks.AIR)) {
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(blockSlot));
                    place(mc.player.getBlockPos(), Direction.DOWN);
                    place(mc.player.getBlockPos().offset(dir), Direction.DOWN);
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(currentSlot));
                }
            }
        }

        super.onTick();
    }

    public static void place(final BlockPos pos, final Direction d) {
        MinecraftClient mc = MinecraftClient.getInstance();
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(Vec3d.ofCenter((Vec3i) pos).add(Vec3d.of(d.getVector()).multiply(0.5)), d.getOpposite(), pos.offset(d), false));
    }
}

package me.bakje.bakjedev.bakjedev.module.Combat;

import me.bakje.bakjedev.bakjedev.module.Mod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Auto32K extends Mod {
    public Auto32K() {
        super("Auto32K", "2019 larp", Category.COMBAT, true);
    }
    int counter =0;
    boolean done = false;
    @Override
    public void onTick() {
        counter++;
        if (counter > 3 && !done) {
            BlockPos pos = ((BlockHitResult) mc.crosshairTarget).getBlockPos();
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND,  new BlockHitResult(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), Direction.UP, pos, false));
            done=true;
        }
        if (mc.player.currentScreenHandler instanceof Generic3x3ContainerScreenHandler) {
            for (int i = 0; i < 45; i++) {
                if( mc.player.currentScreenHandler.slots.get(i).getStack().isOf(Items.SHULKER_BOX)) {
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.QUICK_MOVE, mc.player);

                    mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
                    mc.setScreen(null);
                    mc.player.currentScreenHandler = mc.player.playerScreenHandler;
                }
            }
        }
    }
    @Override
    public void onEnable() {
        super.onEnable();
        done=false;
        BlockPos pos = ((BlockHitResult) mc.crosshairTarget).getBlockPos();



        switchToItem(Items.HOPPER);
        placeHopper(pos);
        switchToItem(Items.DISPENSER);
        placeDispenser(pos);
        switchToItem(Items.REDSTONE_BLOCK);
        placeRedStoneBlock(pos);


        Vec3d f = Vec3d.ofCenter((Vec3i) pos);
        double dX = mc.player.getX() - f.getX();
        double dY = mc.player.getY() - f.subtract(0, 2, 0).getY();
        double dZ = mc.player.getZ() - f.getZ();

        double DistanceXZ = Math.sqrt(dX * dX + dZ * dZ);
        double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + dY * dY);

        double newYaw = Math.acos(dX / DistanceXZ) * 180 / Math.PI;
        double newPitch = Math.acos(dY / -DistanceY) * 180 / Math.PI - 90;

        if (dZ < 0.0)
            newYaw = newYaw + Math.abs(180 - newYaw) * 2;
        newYaw = (newYaw + 90);

        mc.player.setYaw((float) newYaw);
        mc.player.setPitch((float) newPitch);

        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(8));
        mc.player.getInventory().selectedSlot=8;
        done=false;


    }

    public static void switchToItem(Item item) {
        MinecraftClient mc = MinecraftClient.getInstance();
        for (int slot = 0; slot < 9; slot++) {
            ItemStack stack = mc.player.getInventory().getStack(slot);
            if (stack.getItem() == item) {
                int itemSlot = slot;
                mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(itemSlot));
            }
        }
    }
    public static void placeHopper(BlockPos pos) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Direction dir;
        if (mc.player.getMovementDirection()==Direction.NORTH) {
            dir=Direction.SOUTH;
        } else if (mc.player.getMovementDirection()==Direction.EAST) {
            dir=Direction.WEST;
        } else if (mc.player.getMovementDirection()==Direction.SOUTH) {
            dir=Direction.NORTH;
        } else if (mc.player.getMovementDirection()==Direction.WEST) {
            dir=Direction.EAST;
        } else dir=null;

        if (!mc.world.isAir(pos.offset(dir))) {
            return;
        }

        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(Vec3d.ofCenter((Vec3i) pos).add(Vec3d.of(dir.getVector()).multiply(0.5)), dir, pos.offset(dir), false));
    }

    public static void placeDispenser(BlockPos pos) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Direction dir;
        if (mc.player.getMovementDirection()==Direction.NORTH) {
            dir=Direction.SOUTH;
        } else if (mc.player.getMovementDirection()==Direction.EAST) {
            dir=Direction.WEST;
        } else if (mc.player.getMovementDirection()==Direction.SOUTH) {
            dir=Direction.NORTH;
        } else if (mc.player.getMovementDirection()==Direction.WEST) {
            dir=Direction.EAST;
        } else dir=null;


        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(Vec3d.ofCenter((Vec3i) pos).add(Vec3d.of(dir.getVector()).multiply(0.5)), Direction.DOWN, pos.offset(Direction.UP), false));
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(Vec3d.ofCenter((Vec3i) pos).add(Vec3d.of(dir.getVector()).multiply(0.5)), Direction.DOWN, pos.offset(Direction.UP), false));
    }

    public static void placeRedStoneBlock(BlockPos pos) {
        MinecraftClient mc = MinecraftClient.getInstance();

        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(Vec3d.ofCenter((Vec3i) pos).add(Vec3d.of(Direction.UP.getVector()).add(Vec3d.of(Direction.UP.getVector())).multiply(0.5)), Direction.NORTH, pos.offset(Direction.UP).offset(Direction.UP), false));
    }
}
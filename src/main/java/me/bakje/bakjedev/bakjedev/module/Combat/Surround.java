package me.bakje.bakjedev.bakjedev.module.Combat;

import com.sun.jna.platform.win32.OaIdl;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.BooleanSetting;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

public class Surround extends Mod {
    BlockPos playerPos;
    public BooleanSetting jumpDisable = new BooleanSetting("JumpDisable", true);
    public Surround() {
        super("Surround", "obi", Category.COMBAT, true);
        addSetting(jumpDisable);
    }
    @Override
    public void onEnable() {
        playerPos = mc.player.getBlockPos();
        mc.player.updatePosition(mc.player.getBlockX()+0.5, mc.player.getBlockY(), mc.player.getBlockZ()+0.5);
        placeBlock(playerPos);
    }
    @Override
    public void onTick() {
        playerPos = mc.player.getBlockPos();
        antiCrystal();
        placeBlock(playerPos);
        if (mc.options.jumpKey.isPressed() && mc.currentScreen==null && jumpDisable.isEnabled()) {
            this.toggle();
        }
        super.onTick();
    }

    private void placeBlock(final BlockPos pos) {
        boolean northAir=false;
        boolean eastAir=false;
        boolean southAir=false;
        boolean westAir=false;

        if (mc.world.isAir(pos.offset(Direction.NORTH)) || mc.world.getBlockState(pos.offset(Direction.NORTH)).getMaterial().isReplaceable()) {
            northAir=true;
        }
        if (mc.world.isAir(pos.offset(Direction.EAST)) || mc.world.getBlockState(pos.offset(Direction.EAST)).getMaterial().isReplaceable()) {
            eastAir=true;
        }
        if (mc.world.isAir(pos.offset(Direction.SOUTH)) || mc.world.getBlockState(pos.offset(Direction.SOUTH)).getMaterial().isReplaceable()) {
            southAir=true;
        }
        if (mc.world.isAir(pos.offset(Direction.WEST)) || mc.world.getBlockState(pos.offset(Direction.WEST)).getMaterial().isReplaceable()) {
            westAir=true;
        }


        int currentSlot = mc.player.getInventory().selectedSlot;
        for (int slot= 0; slot < 9; slot++){
            ItemStack stack = mc.player.getInventory().getStack(slot);
            if (stack.getItem() == Items.OBSIDIAN) {
                int obiSlot = slot;

                if (northAir) {
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(obiSlot));
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), 180, mc.player.getPitch(), mc.player.isOnGround()));
                    interact(new Vec3d(pos.offset(Direction.NORTH).getX(), pos.offset(Direction.NORTH).getY(), pos.offset(Direction.NORTH).getZ()), Hand.MAIN_HAND, Direction.DOWN);
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(currentSlot));
                }
                if (eastAir) {
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(obiSlot));
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), -90, mc.player.getPitch(), mc.player.isOnGround()));
                    interact(new Vec3d(pos.offset(Direction.EAST).getX(), pos.offset(Direction.EAST).getY(), pos.offset(Direction.EAST).getZ()), Hand.MAIN_HAND, Direction.DOWN);
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(currentSlot));
                }
                if (southAir) {
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(obiSlot));
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), 0, mc.player.getPitch(), mc.player.isOnGround()));
                    interact(new Vec3d(pos.offset(Direction.SOUTH).getX(), pos.offset(Direction.SOUTH).getY(), pos.offset(Direction.SOUTH).getZ()), Hand.MAIN_HAND, Direction.DOWN);
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(currentSlot));
                }
                if (westAir) {
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(obiSlot));
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), 90, mc.player.getPitch(), mc.player.isOnGround()));
                    interact(new Vec3d(pos.offset(Direction.WEST).getX(), pos.offset(Direction.WEST).getY(), pos.offset(Direction.WEST).getZ()), Hand.MAIN_HAND, Direction.DOWN);
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(currentSlot));
                }

            }
        }
    }

    public static void interact(Vec3d vec, Hand hand, Direction direction)
    {
        mc.interactionManager.interactBlock(mc.player, hand, new BlockHitResult(vec, direction, new BlockPos(vec), false));
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    public static void antiCrystal() {
        ClientPlayerEntity player = mc.player;
        Vec3d playerPos = player.getPos();
        double closestDistance = Double.POSITIVE_INFINITY;
        Entity closestEntity = null;
        for (Entity entity : mc.world.getEntities()) {
            Vec3d entityPos = entity.getPos();
            if (!(entity instanceof EndCrystalEntity)) {
                return;
            }
            boolean attackCondition = entityPos.distanceTo(mc.player.getPos()) < closestDistance && entityPos.distanceTo(playerPos) < 2;
            if (attackCondition) {
                closestDistance=entityPos.distanceTo(playerPos);
                closestEntity=entity;
                mc.interactionManager.attackEntity(mc.player, closestEntity);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }
}
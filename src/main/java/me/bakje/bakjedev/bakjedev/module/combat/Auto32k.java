package me.bakje.bakjedev.bakjedev.module.combat;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Streams;

import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.settings.BooleanSetting;
import me.bakje.bakjedev.bakjedev.module.settings.ModeSetting;
import me.bakje.bakjedev.bakjedev.module.settings.NumberSetting;
import me.bakje.bakjedev.bakjedev.util.world.WorldUtil;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.screen.ingame.Generic3x3ContainerScreen;
import net.minecraft.client.gui.screen.ingame.HopperScreen;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;


public class Auto32k extends Mod {
    public BooleanSetting rotate = new BooleanSetting("Rotate", true);
    public BooleanSetting aura = new BooleanSetting("Aura", false);
    public NumberSetting cps = new NumberSetting("CPS", 0, 20, 20, 1);
    public ModeSetting clickUnit = new ModeSetting("ClickUnit","Clicks/Sec", "Clicks/Sec", "Clicks/Tick", "Tick Delay");
    public BooleanSetting auraTimeout = new BooleanSetting("AuraTimeout", false);
    public ModeSetting place = new ModeSetting("Place","Auto", "Auto", "Looking");

    private BlockPos pos;

    private int hopper;
    private int dispenser;
    private int redstone;
    private int shulker;
    private int block;
    private int[] rot;
    private float[] startRot;

    private boolean active;
    private boolean openedDispenser;
    private int dispenserTicks;

    private int ticksPassed;
    private int timer = 0;


    public Auto32k() {
        super("Auto32k", "2020 fortnite 秦皇岛的黑客", Category.COMBAT, true);
        addSettings(rotate, aura, cps, clickUnit, auraTimeout, place);
    }

    @Override
    public void onEnable() {
        if (mc.world == null)
            return;

        super.onEnable();

        ticksPassed = 0;
        hopper = -1;
        dispenser = -1;
        redstone = -1;
        shulker = -1;
        block = -1;
        active = false;
        openedDispenser = false;
        dispenserTicks = 0;
        timer = 0;

        for (int i = 0; i <= 8; i++) {
            Item item = mc.player.getInventory().getStack(i).getItem();
            if (item == Item.fromBlock(Blocks.HOPPER))
                hopper = i;
            else if (item == Item.fromBlock(Blocks.DISPENSER))
                dispenser = i;
            else if (item == Item.fromBlock(Blocks.REDSTONE_BLOCK))
                redstone = i;
            else if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof ShulkerBoxBlock)
                shulker = i;
            else if (item instanceof BlockItem)
                block = i;
        }

        MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);

        if (hopper == -1)
            mc.player.sendMessage(prefixString.append(Text.literal("Missing hopper silly").formatted(Formatting.GRAY)), false);
        else if (dispenser == -1)
            mc.player.sendMessage(prefixString.append(Text.literal("Missing dispenser silly").formatted(Formatting.GRAY)), false);
        else if (redstone == -1)
            mc.player.sendMessage(prefixString.append(Text.literal("Missing redstone block silly").formatted(Formatting.GRAY)), false);
        else if (shulker == -1)
            mc.player.sendMessage(prefixString.append(Text.literal("Missing shulker silly").formatted(Formatting.GRAY)), false);
        else if (block == -1)
            mc.player.sendMessage(prefixString.append(Text.literal("Missing generic block silly").formatted(Formatting.GRAY)), false);

        if (hopper == -1 || dispenser == -1 || redstone == -1 || shulker == -1 || block == -1) {
            setEnabled(false);
            return;
        }

        if (this.place.getIndex() == 1) {
            HitResult ray = mc.player.raycast(5, mc.getTickDelta(), false);
            pos = new BlockPos(ray.getPos().x, Math.ceil(ray.getPos().y), ray.getPos().z);

            double x = pos.getX() - mc.player.getPos().x;
            double z = pos.getZ() - mc.player.getPos().z;

            rot = Math.abs(x) > Math.abs(z) ? x > 0 ? new int[] { -1, 0 } : new int[] { 1, 0 } : z > 0 ? new int[] { 0, -1 } : new int[] { 0, 1 };

            if (!WorldUtil.canPlaceBlock(pos) /* || canPlaceBlock(pos.add(rot[0], 0, rot[1])) */
                    || !WorldUtil.isBlockEmpty(pos)
                    || !WorldUtil.isBlockEmpty(pos.add(rot[0], 0, rot[1]))
                    || !WorldUtil.isBlockEmpty(pos.add(0, 1, 0))
                    || !WorldUtil.isBlockEmpty(pos.add(0, 2, 0))
                    || !WorldUtil.isBlockEmpty(pos.add(rot[0], 1, rot[1]))) {
                mc.player.sendMessage(prefixString.append(Text.literal("Unable to place 32k silly").formatted(Formatting.GRAY)), false);
                setEnabled(false);
                return;
            }

            WorldUtil.placeBlock(pos, block, false, false, true);

            WorldUtil.facePosPacket(
                    pos.add(-rot[0], 1, -rot[1]).getX() + 0.5, pos.getY() + 1, pos.add(-rot[0], 1, -rot[1]).getZ() + 0.5);
            WorldUtil.placeBlock(pos.add(0, 1, 0), dispenser, 0, false, false, true);
            return;

        } else {
            for (int x = -2; x <= 2; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -2; z <= 2; z++) {
                        rot = Math.abs(x) > Math.abs(z) ? x > 0 ? new int[] { -1, 0 } : new int[] { 1, 0 } : z > 0 ? new int[] { 0, -1 } : new int[] { 0, 1 };

                        pos = mc.player.getBlockPos().add(x, y, z);

                        if (mc.player.getPos().add(0, mc.player.getEyeHeight(mc.player.getPose()), 0).distanceTo(
                                mc.player.getPos().add(x - rot[0] / 2, y + 0.5, z + rot[1] / 2)) > 4.5
                                || mc.player.getPos().add(0, mc.player.getEyeHeight(mc.player.getPose()), 0).distanceTo(
                                mc.player.getPos().add(x + 0.5, y + 2.5, z + 0.5)) > 4.5
                                || !(WorldUtil.canPlaceBlock(pos) /* || canPlaceBlock(pos.add(rot[0], 0, rot[1])) */)
                                || !WorldUtil.isBlockEmpty(pos)
                                || !WorldUtil.isBlockEmpty(pos.add(rot[0], 0, rot[1]))
                                || !WorldUtil.isBlockEmpty(pos.add(0, 1, 0))
                                || !WorldUtil.isBlockEmpty(pos.add(0, 2, 0))
                                || !WorldUtil.isBlockEmpty(pos.add(rot[0], 1, rot[1])))
                            continue;

                        startRot = new float[] { mc.player.getYaw(), mc.player.getPitch() };
                        WorldUtil.facePos(pos.add(-rot[0], 1, -rot[1]).getX() + 0.5, pos.getY() + 1, pos.add(-rot[0], 1, -rot[1]).getZ() + 0.5);
                        WorldUtil.facePosPacket(pos.add(-rot[0], 1, -rot[1]).getX() + 0.5, pos.getY() + 1, pos.add(-rot[0], 1, -rot[1]).getZ() + 0.5);
                        return;
                    }
                }
            }
        }

        mc.player.sendMessage(prefixString.append(Text.literal("Unable to place 32k silly").formatted(Formatting.GRAY)), false);
        setEnabled(false);
    }

    @Override
    public void onTick() {
        if ((this.auraTimeout.isEnabled() && !active && ticksPassed > 25) || (active && !(mc.currentScreen instanceof HopperScreen))) {
            setEnabled(false);
            return;
        }

        if (ticksPassed == 1) {
            // boolean rotate = getSetting(0).toToggle().state;

            WorldUtil.placeBlock(pos, block, 0, false, false, true);
            WorldUtil.placeBlock(pos.add(0, 1, 0), dispenser, 0, false, false, true);
            mc.player.setYaw(startRot[0]);
            mc.player.setPitch(startRot[1]);

            ticksPassed++;
            return;
        }

        if (active && this.aura.isEnabled() && timer == 0)
            killAura();

        if (mc.currentScreen instanceof Generic3x3ContainerScreen) {
            openedDispenser = true;
        }

        if (mc.currentScreen instanceof HopperScreen) {
            HopperScreen gui = (HopperScreen) mc.currentScreen;

            for (int i = 32; i <= 40; i++) {
                // System.out.println(EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS,
                // gui.inventorySlots.getSlot(i).getStack()));
                if (EnchantmentHelper.getLevel(Enchantments.SHARPNESS, gui.getScreenHandler().getSlot(i).getStack()) > 5) {
                    mc.player.getInventory().selectedSlot = i - 32;
                    break;
                }
            }

            active = true;

            if (active) {
                if (this.clickUnit.getIndex() == 0) {
                    timer = timer >= Math.round(20 / this.cps.getValue()) ? 0 : timer + 1;
                } else if (this.clickUnit.getIndex() == 1) {
                    timer = 0;
                } else if (this.clickUnit.getIndex() == 2) {
                    timer = timer >= this.cps.getValue() ? 0 : timer + 1;
                }
            }

            // System.out.println(EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS,
            // mc.player.inventory.getCurrentItem()));
            if (!(gui.getScreenHandler().getSlot(0).getStack().getItem() instanceof AirBlockItem) && active) {
                mc.interactionManager.clickSlot(gui.getScreenHandler().syncId, 0, mc.player.getInventory().selectedSlot, SlotActionType.SWAP, mc.player);
            }
        }

        if (ticksPassed == 2) {
            openBlock(pos.add(0, 1, 0));
        }

        if (openedDispenser && dispenserTicks == 0) {
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 36 + shulker, 0, SlotActionType.QUICK_MOVE, mc.player);
        }

        if (dispenserTicks == 1) {
            mc.setScreen(null);
            WorldUtil.placeBlock(pos.add(0, 2, 0), redstone, false, false, true);
        }

        if (mc.world.getBlockState(pos.add(rot[0], 1, rot[1])).getBlock() instanceof ShulkerBoxBlock
                && mc.world.getBlockState(pos.add(rot[0], 0, rot[1])).getBlock() != Blocks.HOPPER) {
            WorldUtil.placeBlock(pos.add(rot[0], 0, rot[1]), hopper, false, false, true);
            openBlock(pos.add(rot[0], 0, rot[1]));
        }

        if (openedDispenser)
            dispenserTicks++;
        ticksPassed++;
    }

    private void killAura() {
        for (int i = 0; i < (this.clickUnit.getIndex() == 1 ? this.cps.getValue() : 1); i++) {
            Entity target = null;

            List<Entity> players = Streams.stream(mc.world.getEntities())
                    .filter(e -> e instanceof PlayerEntity && e != mc.player && !(Bakjedev.friendMang.has(e.getName().getString())))
                    .sorted((a, b) -> Double.compare(a.squaredDistanceTo(mc.player), b.squaredDistanceTo(mc.player)))
                    .collect(Collectors.toList());

            if (!players.isEmpty() && players.get(0).getPos().distanceTo(mc.player.getPos()) < 8) {
                target = players.get(0);
            } else {
                return;
            }

            WorldUtil.facePos(target.getPos().x, target.getPos().y + 1, target.getPos().z);

            if (target.getPos().distanceTo(mc.player.getPos()) > 6)
                return;
            mc.interactionManager.attackEntity(mc.player, target);
        }
    }

    private void openBlock(BlockPos pos) {
        for (Direction d : Direction.values()) {
            if (mc.world.getBlockState(pos.offset(d)).getMaterial().isReplaceable()) {
                mc.interactionManager.interactBlock(
                        mc.player, Hand.MAIN_HAND, new BlockHitResult(Vec3d.of(pos), d.getOpposite(), pos, false));
                return;
            }
        }
    }


}

package me.bakje.bakjedev.bakjedev.module.Misc;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.ModeSetting;
import me.bakje.bakjedev.bakjedev.module.Settings.NumberSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;


public class ChestSwap extends Mod {
    public ChestSwap() {
        super("ChestSwap", "swaps chestplate with elytra or vice versa", Category.MISC, true);
    }
    @Override
    public void onEnable() {
        ItemStack chestSlot = mc.player.getInventory().getStack(38);
        if (chestSlot.isOf(Items.ELYTRA)) {
            if (mc.player.playerScreenHandler == mc.player.currentScreenHandler) {
                for (int i = 9; i < 45; i++) {
                    if (mc.player.getInventory().getStack(i >=36 ? i -36 : i).getItem()==Items.NETHERITE_CHESTPLATE
                    || mc.player.getInventory().getStack(i >=36 ? i -36 : i).getItem()==Items.DIAMOND_CHESTPLATE
                    || mc.player.getInventory().getStack(i >=36 ? i -36 : i).getItem()==Items.IRON_CHESTPLATE
                    || mc.player.getInventory().getStack(i >=36 ? i -36 : i).getItem()==Items.GOLDEN_CHESTPLATE
                    || mc.player.getInventory().getStack(i >=36 ? i -36 : i).getItem()==Items.CHAINMAIL_CHESTPLATE
                    || mc.player.getInventory().getStack(i >=36 ? i -36 : i).getItem()==Items.LEATHER_CHESTPLATE) {
                        boolean itemInChestSlot = !mc.player.getInventory().getStack(38).isEmpty();
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 6, 0, SlotActionType.PICKUP, mc.player);

                        if (itemInChestSlot)
                            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                        this.toggle();
                        return;
                    }
                }
            } else {
                for (int i = 0; i < 9; i++) {
                    if (mc.player.getInventory().getStack(i).getItem() == Items.NETHERITE_CHESTPLATE
                    || mc.player.getInventory().getStack(i).getItem() == Items.DIAMOND_CHESTPLATE
                    || mc.player.getInventory().getStack(i).getItem() == Items.IRON_CHESTPLATE
                    || mc.player.getInventory().getStack(i).getItem() == Items.GOLDEN_CHESTPLATE
                    || mc.player.getInventory().getStack(i).getItem() == Items.CHAINMAIL_CHESTPLATE
                    || mc.player.getInventory().getStack(i).getItem() == Items.LEATHER_CHESTPLATE) {
                        if (i != mc.player.getInventory().selectedSlot) {
                            mc.player.getInventory().selectedSlot = i;
                            mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(i));
                        }

                        mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
                        this.toggle();
                        return;
                    }
                }
            }
        } else if (!chestSlot.isEmpty() && !chestSlot.isOf(Items.ELYTRA)) {
            if (mc.player.playerScreenHandler == mc.player.currentScreenHandler) {
                for (int i = 9; i < 45; i++) {
                    if (mc.player.getInventory().getStack(i >=36 ? i -36 : i).getItem()==Items.ELYTRA) {
                        boolean itemInChestSlot = !mc.player.getInventory().getStack(38).isEmpty();
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 6, 0, SlotActionType.PICKUP, mc.player);

                        if (itemInChestSlot)
                            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                        this.toggle();
                        return;
                    }
                }
            } else {
                for (int i = 0; i < 9; i++) {
                    if (mc.player.getInventory().getStack(i).getItem() == Items.ELYTRA) {
                        if (i != mc.player.getInventory().selectedSlot) {
                            mc.player.getInventory().selectedSlot = i;
                            mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(i));
                        }

                        mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
                        this.toggle();
                        return;
                    }
                }
            }
        }

        super.onEnable();
    }

}
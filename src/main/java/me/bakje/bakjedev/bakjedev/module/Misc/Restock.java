package me.bakje.bakjedev.bakjedev.module.Misc;


import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.NumberSetting;
import me.bakje.bakjedev.bakjedev.util.BakjePair;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

import java.util.HashMap;
import java.util.Map;


public class Restock extends Mod {
    public NumberSetting threshold = new NumberSetting("Threshold", 1, 63, 32, 1);
    private static Map<Integer, ItemStack> getInventory() {
        return getInventorySlots(9, 35);
    }

    private static Map<Integer, ItemStack> getHotbar() {
        return getInventorySlots(36, 44);
    }

    public static Map<Integer, ItemStack> getInventorySlots(int current, final int last) {
        final Map<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        while (current <= last) {
            fullInventorySlots.put(current, (ItemStack) mc.player.getInventory().getStack(current));
            ++current;
        }
        return fullInventorySlots;
    }
    public Restock() {
        super("Restock", "refill items", Category.MISC, true);
    }

    @Override
    public void onTick() {
        if (mc.currentScreen instanceof InventoryScreen)
            return;
        final BakjePair<Integer, Integer> slots = findReplenishableHotbarSlot();
        if (slots==null) return;
        final int inventorySlot = slots.getKey();
        final int hotbarSlot = slots.getValue();
        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, inventorySlot, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, hotbarSlot, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, inventorySlot, 0, SlotActionType.PICKUP, mc.player);

    }


    private BakjePair<Integer, Integer> findReplenishableHotbarSlot() {
        BakjePair<Integer, Integer> returnPair = null;
        for (final Map.Entry<Integer, ItemStack> hotbarSlot : getHotbar().entrySet()) {
            final ItemStack stack = hotbarSlot.getValue();
            if (!stack.isEmpty()) {
                if (stack.getItem() == Items.AIR) {
                    continue;
                }
                if (!stack.isStackable()) {
                    continue;
                }
                if (stack.getCount() >= stack.getMaxCount()) {
                    continue;
                }
                if (stack.getCount() > this.threshold.getValue()) {
                    continue;
                }
                final int inventorySlot = findCompatibleInventorySlot(stack);
                if (inventorySlot == -1) {
                    continue;
                }
                returnPair = new BakjePair<>(inventorySlot, hotbarSlot.getKey());
            }
        }
        return returnPair;
    }

    private int findCompatibleInventorySlot(final ItemStack hotbarStack) {
        int inventorySlot = -1;
        int smallestStackSize = 999;
        for (final Map.Entry<Integer, ItemStack> entry : getInventory().entrySet()) {
            final ItemStack inventoryStack = entry.getValue();
            if (!inventoryStack.isEmpty()) {
                if (inventoryStack.getItem() == Items.AIR) {
                    continue;
                }
                if (!this.isCompatibleStacks(hotbarStack, inventoryStack)) {
                    continue;
                }
                final int currentStackSize = mc.player.getInventory().getStack(entry.getKey()).getCount();
                if (smallestStackSize <= currentStackSize) {
                    continue;
                }
                smallestStackSize = currentStackSize;
                inventorySlot = entry.getKey();
            }
        }
        return inventorySlot;
    }

    private boolean isCompatibleStacks(final ItemStack stack1, final ItemStack stack2) {
        if (!stack1.getItem().equals(stack2.getItem())) {
            return false;
        }
        if (stack1.getItem() instanceof BlockItem && stack2.getItem() instanceof BlockItem) {
            final Block block1 = ((BlockItem)stack1.getItem()).getBlock();
            final Block block2 = ((BlockItem)stack2.getItem()).getBlock();
            if (!block1.getName().equals(block2.getName())) {
                return false;
            }
        }
        return stack1.getName().equals(stack2.getName()) && stack1.getDamage() == stack2.getDamage();
    }
}
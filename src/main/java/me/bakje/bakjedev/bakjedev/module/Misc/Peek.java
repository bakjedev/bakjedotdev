package me.bakje.bakjedev.bakjedev.module.Misc;

import com.google.common.collect.Maps;
import me.bakje.bakjedev.bakjedev.module.Mod;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;

public class Peek extends Mod {

    public Peek() {
        super("Peek", "look inside shulker", Category.MISC, true);
    }
    private ShulkerBoxScreen shulkerBoxScreen;
    int counter;
    boolean openShulker;

    @Override
    public void onEnable() {
        counter=0;
        super.onEnable();
    }

    @Override
    public void onTick() {
        if (counter<1) {
            counter++;
            ItemStack stack = mc.player.getMainHandStack();
            ShulkerBoxScreenHandler shulkerBoxScreenHandler = new ShulkerBoxScreenHandler(0, mc.player.getInventory());
            if (!stack.isEmpty() && (stack.getItem() == Items.SHULKER_BOX || stack.getItem() == Items.BLACK_SHULKER_BOX || stack.getItem() == Items.BLUE_SHULKER_BOX || stack.getItem() == Items.BROWN_SHULKER_BOX || stack.getItem() == Items.CYAN_SHULKER_BOX || stack.getItem() == Items.GRAY_SHULKER_BOX || stack.getItem() == Items.GREEN_SHULKER_BOX || stack.getItem() == Items.LIGHT_BLUE_SHULKER_BOX || stack.getItem() == Items.LIGHT_GRAY_SHULKER_BOX || stack.getItem() == Items.LIME_SHULKER_BOX || stack.getItem() == Items.MAGENTA_SHULKER_BOX || stack.getItem() == Items.ORANGE_SHULKER_BOX || stack.getItem() == Items.PINK_SHULKER_BOX || stack.getItem() == Items.PURPLE_SHULKER_BOX || stack.getItem() == Items.RED_SHULKER_BOX || stack.getItem() == Items.WHITE_SHULKER_BOX || stack.getItem() == Items.YELLOW_SHULKER_BOX)) {
                openShulker=true;
                HashMap<Integer, ItemStack> stackHashMap = getStacksFromShulker(stack);
                stackHashMap.keySet().forEach(slot -> {
                    shulkerBoxScreenHandler.setStackInSlot(slot, shulkerBoxScreenHandler.nextRevision(), stackHashMap.get(slot));
                });
                shulkerBoxScreen = new ShulkerBoxScreen(shulkerBoxScreenHandler, mc.player.getInventory(), stack.getName());
            } else {
                openShulker=false;
                MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
                mc.player.sendMessage(prefixString.copy().append(Text.literal("You must be holding a Shulker Box to use this module").formatted(Formatting.GRAY)));
            }
        } else {
            if (mc.currentScreen==null && openShulker) {
                mc.setScreen(shulkerBoxScreen);
                //fun feature! if you press q on the shulker while peeked it crashes your game!
            }
            this.toggle();
        }
        super.onTick();
    }

    public HashMap<Integer, ItemStack> getStacksFromShulker(ItemStack shulkerBox) {
        HashMap<Integer, ItemStack> stacks = Maps.newHashMap();
        NbtCompound nbttagcompound = shulkerBox.getNbt();
        if (nbttagcompound == null) return stacks;


        NbtCompound nbttagcompound1 = nbttagcompound.getCompound("BlockEntityTag");
        for (int i = 0; i < nbttagcompound1.getList("Items", 10).size(); i++) {
            NbtCompound compound = nbttagcompound1.getList("Items", 10).getCompound(i);
            int slot = compound.getInt("Slot");
            ItemStack itemStack = ItemStack.fromNbt(compound);
            stacks.put(slot, itemStack);
        }
        return stacks;
    }
}

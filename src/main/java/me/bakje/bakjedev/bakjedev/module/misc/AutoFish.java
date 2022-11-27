package me.bakje.bakjedev.bakjedev.module.misc;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.settings.ModeSetting;
import me.bakje.bakjedev.bakjedev.module.settings.NumberSetting;
import me.bakje.bakjedev.bakjedev.util.InventoryUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.apache.commons.lang3.RandomUtils;

import java.util.Comparator;


public class AutoFish extends Mod {
    public ModeSetting fishMode = new ModeSetting("Mode","Normal", "Normal", "Aggressive", "Passive"); //0
    public NumberSetting reelDelay = new NumberSetting("reelDelay", 0, 100, 2, 1); //1
    public NumberSetting randReelDelay = new NumberSetting("randReelDelay", 0, 40, 2, 1); //2
    public NumberSetting castDelay = new NumberSetting("castDelay", 0, 100, 2, 1); //3
    public NumberSetting randCastDelay = new NumberSetting("randCastDelay", 0, 40, 2, 1); //4

    private boolean threwRod;
    private boolean reeledFish;

    private int currentTime = 0;
    private int randReel = RandomUtils.nextInt(0, randReelDelay.getValueInt() + 1);
    private int randCast = RandomUtils.nextInt(0, randCastDelay.getValueInt() + 1);
    private int fishSpotted = 0;
    public AutoFish() {
        super("AutoFish", "fishes for you", Category.MISC, true);
        addSettings(fishMode, reelDelay, randReelDelay, castDelay, randCastDelay);
    }
    @Override
    public void onDisable() {
        threwRod=false;
        reeledFish=false;
        super.onDisable();
    }

    @Override
    public void onTick() {
        currentTime++;
        if (mc.player.fishHook != null) {
            threwRod = false;

            boolean caughtFish = mc.player.fishHook.getDataTracker().get(FishingBobberEntity.CAUGHT_FISH);
            if (!reeledFish && caughtFish) {
                Hand hand = getHandWithRod();
                if (hand != null) {
                    // reel back
                    if ( currentTime >= randReel + reelDelay.getValue() + fishSpotted) {
                        randReel = RandomUtils.nextInt(0, randReelDelay.getValueInt() + 1);
                        mc.interactionManager.interactItem(mc.player, hand);
                        reeledFish = true;
                        return;
                    }
                }
            } else if (!caughtFish) {
                reeledFish = false;
                fishSpotted = currentTime;
            }
        }

        if (!threwRod && mc.player.fishHook == null && fishMode.getIndex()!=2) {
            Hand newHand = fishMode.getIndex()==1 ? InventoryUtil.selectSlot(getBestRodSlot()) : getHandWithRod();
            if (newHand != null) {
                // throw again
                if ( currentTime >= randCast + reelDelay.getValue() + castDelay.getValue() + fishSpotted) {
                    randCast = RandomUtils.nextInt(0, randCastDelay.getValueInt() + 1);
                    mc.interactionManager.interactItem(mc.player, newHand);
                    threwRod = true;
                    reeledFish = false;
                }
            }
        }
    }

    private Hand getHandWithRod() {
        return mc.player.getMainHandStack().getItem() == Items.FISHING_ROD ? Hand.MAIN_HAND
                : mc.player.getOffHandStack().getItem() == Items.FISHING_ROD ? Hand.OFF_HAND
                : null;
    }

    private int getBestRodSlot() {
        int slot = InventoryUtil.getSlot(true, true, Comparator.comparingInt(i -> {
            ItemStack is = mc.player.getInventory().getStack(i);
            if (is.getItem() != Items.FISHING_ROD)
                return -1;

            return EnchantmentHelper.get(is).values().stream().mapToInt(Integer::intValue).sum();
        }));

        if (mc.player.getInventory().getStack(slot).getItem() == Items.FISHING_ROD) {
            return slot;
        }

        return -1;
    }
}
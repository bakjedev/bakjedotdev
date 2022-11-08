package me.bakje.bakjedev.bakjedev.module.Combat;

import me.bakje.bakjedev.bakjedev.event.events.PacketEvent;
import me.bakje.bakjedev.bakjedev.event.events.PlayerPushedEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.slot.SlotActionType;


public class Grab32k extends Mod {
    public Grab32k() {
        super("32kGrab", "take all 32k", Category.COMBAT, true);
    }

    @Override
    public void onTick() {
        if (mc.player.currentScreenHandler instanceof HopperScreenHandler) {
            for (int i = 0; i<5; i++) {
                if (mc.player.currentScreenHandler.slots.get(i).getStack().isOf(Items.DIAMOND_SWORD) && mc.player.getInventory().getStack(8).isEmpty()) {
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.QUICK_MOVE, mc.player);
                }
            }
        }
    }
}

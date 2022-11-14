package me.bakje.bakjedev.bakjedev.module.Misc;

import me.bakje.bakjedev.bakjedev.module.Mod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;


public class PartyChat extends Mod {
    public PartyChat() {
        super("PartyChat", "for constantiam lol", Category.MISC, true);
    }
    @Override
    public void onEnable() {
        mc.player.sendCommand("party chat on");
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.player.sendCommand("party chat off");
        super.onDisable();
    }

}
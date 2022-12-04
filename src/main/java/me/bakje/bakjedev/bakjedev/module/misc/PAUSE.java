package me.bakje.bakjedev.bakjedev.module.misc;

import me.bakje.bakjedev.bakjedev.event.events.PacketEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.text.Text;


public class PAUSE extends Mod {
    public PAUSE() {
        super("PAUSE", "baritone", Category.MISC, true);
    }

    @Override
    public void onEnable() {
        mc.player.sendChatMessage("#pause", Text.empty());
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.player.sendChatMessage("#resume", Text.empty());
        super.onDisable();
    }
}
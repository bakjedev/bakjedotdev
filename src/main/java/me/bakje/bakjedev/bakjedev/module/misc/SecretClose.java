package me.bakje.bakjedev.bakjedev.module.misc;

import me.bakje.bakjedev.bakjedev.event.events.PacketEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;


public class SecretClose extends Mod {
    public SecretClose() {
        super("SecretClose", "cancel container close packet", Category.MISC, true);
    }

    @BakjeSubscribe
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CloseHandledScreenC2SPacket) {
            event.setCancelled(true);
        }
    }
}
package me.bakje.bakjedev.bakjedev.module.combat;

import me.bakje.bakjedev.bakjedev.event.events.PacketEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;


public class PopIdentifier extends Mod {
    public PopIdentifier() {
        super("PopIdentifier", "identify player who pops a totem", Category.COMBAT, true);
    }
    @BakjeSubscribe
    public void onReceivePacket(PacketEvent.Read event) {
        if (!(event.getPacket() instanceof EntityStatusS2CPacket p )) return;

        if (p.getStatus() != 35) return;

        Entity e = p.getEntity(mc.world);

        if (!(e instanceof PlayerEntity)) return;

        MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
        mc.player.sendMessage(prefixString.append(Text.literal(e.getName().getString()+" popped").formatted(Formatting.GRAY)), false);
    }
}
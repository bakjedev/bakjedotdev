package me.bakje.bakjedev.bakjedev.module.Misc;

import me.bakje.bakjedev.bakjedev.module.Mod;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.profiling.jfr.event.PacketEvent;

public class Spin extends Mod {
    float counter;
    public Spin() {
        super("Spin", "bad spin thing", Category.MISC);

    }
    @Override
    public void onEnable() {
        float counter = mc.player.getYaw();
        super.onEnable();
    }
    @Override
    public void onTick() {
        counter=counter+10;
        if (counter > 360) {
            counter=0;
        }
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), counter, mc.player.getPitch(), mc.player.isOnGround()));
    super.onTick();
    }
}

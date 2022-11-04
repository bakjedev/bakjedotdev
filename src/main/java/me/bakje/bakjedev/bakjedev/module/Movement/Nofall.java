package me.bakje.bakjedev.bakjedev.module.Movement;

import me.bakje.bakjedev.bakjedev.module.Mod;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Nofall extends Mod {
    public Nofall() {
        super("Nofall", "no more fall damage", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player.fallDistance > 2.5f) {
            if (mc.player.isFallFlying())
                return;
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
        }
    }
}

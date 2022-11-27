package me.bakje.bakjedev.bakjedev.module.movement;

import me.bakje.bakjedev.bakjedev.event.events.ClientMoveEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class FastAccel extends Mod {
    public FastAccel() {
        super("FastAccel", "fast walk", Category.MOVEMENT, true);
    }

    @Override
    public void onTick() {
        if ((mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0) && mc.player.isOnGround()) {
        if (!mc.player.isSprinting()) {
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
        }

        mc.player.setVelocity(new Vec3d(0, mc.player.getVelocity().y, 0));
        mc.player.updateVelocity(0.2F,
                new Vec3d(mc.player.sidewaysSpeed, 0, mc.player.forwardSpeed));

        double vel = Math.abs(mc.player.getVelocity().getX()) + Math.abs(mc.player.getVelocity().getZ());
     }

    }
    @BakjeSubscribe
    public void onMove(ClientMoveEvent event) {
        if (mc.player.forwardSpeed==0 && mc.player.sidewaysSpeed == 0) {
            event.setVec(new Vec3d(0, event.getVec().y, 0));
        }
    }
}



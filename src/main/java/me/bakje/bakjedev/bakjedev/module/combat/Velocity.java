package me.bakje.bakjedev.bakjedev.module.combat;

import me.bakje.bakjedev.bakjedev.event.events.PacketEvent;
import me.bakje.bakjedev.bakjedev.event.events.PlayerPushedEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;


public class Velocity extends Mod {
    public Velocity() {
        super("Velocity", "no knockback", Category.COMBAT, true);
    }

    @BakjeSubscribe
    public void onPlayerPushed(PlayerPushedEvent event) {
        double amount = 0;
        event.setPushX(event.getPushX()*amount);
        event.setPushY(event.getPushY()*amount);
        event.setPushZ(event.getPushZ()*amount);
    }

    @BakjeSubscribe
    public void onReadPacket(PacketEvent.Read event) {
        if (mc.player == null)
            return;
        if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket) {
            EntityVelocityUpdateS2CPacket packet = (EntityVelocityUpdateS2CPacket) event.getPacket();
            if (packet.getId()==mc.player.getId()) {
                double velXZ = 0;
                double velY = 0;

                double pvelX = (packet.getVelocityX() / 8000d - mc.player.getVelocity().x) * velXZ;
                double pvelY = (packet.getVelocityY() / 8000d - mc.player.getVelocity().y) * velY;
                double pvelZ = (packet.getVelocityZ() / 8000d - mc.player.getVelocity().z) * velXZ;

                packet.velocityX = (int) (pvelX * 8000 + mc.player.getVelocity().x * 8000);
                packet.velocityY = (int) (pvelY * 8000 + mc.player.getVelocity().y * 8000);
                packet.velocityZ = (int) (pvelZ * 8000 + mc.player.getVelocity().z * 8000);
            }
        } else if (event.getPacket() instanceof ExplosionS2CPacket) {
            ExplosionS2CPacket packet = (ExplosionS2CPacket) event.getPacket();

            double velXZ =0;
            double velY = 0;

            packet.playerVelocityX = (float) (packet.getPlayerVelocityX() * velXZ);
            packet.playerVelocityY = (float) (packet.getPlayerVelocityY() * velY);
            packet.playerVelocityZ = (float) (packet.getPlayerVelocityZ() * velXZ);
        }
    }
}

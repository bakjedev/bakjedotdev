package me.bakje.bakjedev.bakjedev.module.combat;

import me.bakje.bakjedev.bakjedev.event.events.PacketEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.util.PlayerInteractEntityC2SUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;


public class Criticals extends Mod {
    public Criticals() {
        super("Criticals", "stronk", Category.COMBAT, true);
    }

    @BakjeSubscribe
    public void sendPacket(PacketEvent event) {
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket) {
            PlayerInteractEntityC2SPacket packet = (PlayerInteractEntityC2SPacket) event.getPacket();
            if (PlayerInteractEntityC2SUtil.getEntity(packet) instanceof LivingEntity) {
                critical();
            }
        }
    }

    private void critical() {
        if (mc.player.isClimbing() || mc.player.isTouchingWater()
                || mc.player.hasStatusEffect(StatusEffects.BLINDNESS) || mc.player.hasVehicle()) {
            return;
        }

        boolean sprinting = mc.player.isSprinting();
        if (sprinting) {
            mc.player.setSprinting(false);
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
        }

        if (mc.player.isOnGround()) {
            double x = mc.player.getX();
            double y = mc.player.getY();
            double z = mc.player.getZ();

            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.0633, z, false));
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false));
        }

        if (sprinting) {
            mc.player.setSprinting(true);
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
        }
    }
}

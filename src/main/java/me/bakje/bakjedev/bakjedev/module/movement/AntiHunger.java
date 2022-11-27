package me.bakje.bakjedev.bakjedev.module.movement;

import me.bakje.bakjedev.bakjedev.event.events.PacketEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.mixin.PlayerMoveC2SPacketAccessor;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.settings.ModeSetting;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class AntiHunger extends Mod {
    public ModeSetting ahMode = new ModeSetting("Mode","Meteor", "Meteor", "Bleach");
    private boolean lastOnGround;
    private boolean sendOnGroundTruePacket;
    private boolean ignorePacket;
    public AntiHunger() {
        super("AntiHunger", "food is no more", Category.MOVEMENT, true);
        addSetting(ahMode);
    }

    @Override
    public void onEnable() {
        if (ahMode.isMode("Meteor")) {
            lastOnGround = mc.player.isOnGround();
            sendOnGroundTruePacket = true;
        }
        super.onEnable();
    }

    @Override
    public void onTick() {
        if (ahMode.isMode("Meteor")) {
            if (mc.player.isTouchingWater()) {
                ignorePacket = true;
                return;
            }
            if (mc.player.isOnGround() && !lastOnGround && !sendOnGroundTruePacket) sendOnGroundTruePacket =true;

            if (mc.player.isOnGround() && sendOnGroundTruePacket) {
                ignorePacket= true;
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
                ignorePacket = false;

                sendOnGroundTruePacket = false;
            }
            lastOnGround = mc.player.isOnGround();
        }
        super.onTick();
    }

    @BakjeSubscribe
    public void onSendPacket(PacketEvent.Send event) {
        if (ahMode.getIndex()==0) {
            if (ignorePacket) return;

            if (event.getPacket() instanceof ClientCommandC2SPacket) {
                ClientCommandC2SPacket.Mode mode = ((ClientCommandC2SPacket) event.getPacket()).getMode();

                if (mode==ClientCommandC2SPacket.Mode.START_SPRINTING || mode == ClientCommandC2SPacket.Mode.STOP_SPRINTING) {
                    ((PlayerMoveC2SPacketAccessor) event.getPacket()).setOnGround(false);
                }
            }

            if (event.getPacket() instanceof PlayerMoveC2SPacket && mc.player.isOnGround() && mc.player.fallDistance <= 0.0 && !mc.interactionManager.isBreakingBlock()) {
                event.getPacket();
            }
        }


        if (ahMode.getIndex()==1) {
            if (event.getPacket() instanceof PlayerMoveC2SPacket) {
                if (mc.player.getVelocity().y != 0 && !mc.options.jumpKey.isPressed()) {
                    boolean onGround = mc.player.fallDistance >= 0.1f;
                    mc.player.setOnGround(onGround);
                    ((PlayerMoveC2SPacket) event.getPacket()).onGround = onGround;
                }
            }
        }
    }
}

package me.bakje.bakjedev.bakjedev.module.Movement;

import me.bakje.bakjedev.bakjedev.event.events.ClientMoveEvent;
import me.bakje.bakjedev.bakjedev.event.events.OpenScreenEvent;
import me.bakje.bakjedev.bakjedev.event.events.PacketEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.NumberSetting;
import me.bakje.bakjedev.bakjedev.util.PlayerCopyEntity;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Freecam extends Mod {
    private PlayerCopyEntity dummy;
    private double[] playerPos;
    private float[] playerRot;
    private Entity riding;

    private boolean prevFlying;
    private float prevFlySpeed;
    public NumberSetting speed = new NumberSetting("Speed", 0, 3, 0.5, 0.01);

    public Freecam() {
        super("Freecam", "spectator mode", Category.MOVEMENT, true);
        addSetting(speed);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        mc.chunkCullingEnabled = false;

        playerPos = new double[] { mc.player.getX(), mc.player.getY(), mc.player.getZ() };
        playerRot = new float[] { mc.player.getYaw(), mc.player.getPitch() };

        dummy = new PlayerCopyEntity(mc.player);

        dummy.spawn();

        if (mc.player.getVehicle() != null) {
            riding = mc.player.getVehicle();
            mc.player.getVehicle().removeAllPassengers();
        }

        if (mc.player.isSprinting()) {
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
        }

        prevFlying = mc.player.getAbilities().flying;
        prevFlySpeed = mc.player.getAbilities().getFlySpeed();
    }

    @Override
    public void onDisable() {
        mc.chunkCullingEnabled = true;

        dummy.despawn();
        mc.player.noClip = false;
        mc.player.getAbilities().flying = prevFlying;
        mc.player.getAbilities().setFlySpeed(prevFlySpeed);

        mc.player.refreshPositionAndAngles(playerPos[0], playerPos[1], playerPos[2], playerRot[0], playerRot[1]);
        mc.player.setVelocity(Vec3d.ZERO);

        if (riding != null && mc.world.getEntityById(riding.getId()) != null) {
            mc.player.startRiding(riding);
        }
        super.onDisable();
    }

    @Override
    public void onTick() {
        if (mc.interactionManager.isBreakingBlock()) {
            mc.player.setOnGround(true);
        } else {
            mc.player.setOnGround(false);
        }
        mc.player.getAbilities().setFlySpeed((float) (this.speed.getValue() / 5));
        mc.player.getAbilities().flying=true;
        mc.player.setPose(EntityPose.STANDING);
        super.onTick();
    }

    @BakjeSubscribe
    public void sendPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof ClientCommandC2SPacket || event.getPacket() instanceof PlayerMoveC2SPacket) {
            event.setCancelled(true);
        }
    }

    @BakjeSubscribe
    public void onOpenScreen(OpenScreenEvent event) {
        if (riding instanceof AbstractHorseEntity) {
            if (event.getScreen() instanceof InventoryScreen) {
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.OPEN_INVENTORY));
                event.setCancelled(true);
            }
        }
    }

    @BakjeSubscribe
    public void onClientMove(ClientMoveEvent event) {
        mc.player.noClip=true;
    }
}

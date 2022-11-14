package me.bakje.bakjedev.bakjedev.module.Movement;

import me.bakje.bakjedev.bakjedev.event.events.PacketEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.Settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;

public class BoatFly extends Mod {
    public NumberSetting speed = new NumberSetting("Speed", 0, 5, 2, 1.2);
    public NumberSetting ascend = new NumberSetting("Ascend", 0, 2, 2, 0.3);
    public NumberSetting descend = new NumberSetting("Descend", 0, 2, 0.1, 0.1);
    public BoatFly() {
        super("Boatfly", "boat flying like bruh", Category.MOVEMENT, true);
        addSettings(ascend, descend, speed);
    }

    @Override
    public void onTick() {
        if (mc.player.getVehicle() ==null) return;

        Entity boat = mc.player.getVehicle();
        double speed = ModuleManager.INSTANCE.getModule(BoatFly.class).speed.getValue();

        double forward = mc.player.forwardSpeed;
        double strafe = mc.player.sidewaysSpeed;
        float yaw = mc.player.getYaw();

        boat.setYaw(yaw);
        if (forward !=0.0D) {
            if (strafe > 0.0D) {
                yaw+= (forward>0.0D ? -45 : 45);
            } else if (strafe<0.0D) {
                yaw += (forward > 0.0D ? 45 : -45);
            }

            if (forward>0.0D) {
                forward=1.0D;
            } else if (forward <0.0D) {
                forward = -1.0D;
            }

            strafe = 0.0D;
        }

        boat.setVelocity(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)),
                boat.getVelocity().y,
                forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));



        if (mc.options.jumpKey.isPressed()) {
            boat.setVelocity(boat.getVelocity().x, ascend.getValue(), boat.getVelocity().z);
        } else {
            boat.setVelocity(boat.getVelocity().x, -descend.getValue(), boat.getVelocity().z);
        }
        super.onTick();
    }

    @BakjeSubscribe
    public void onReadPacket(PacketEvent.Read event) {
        if (mc.player !=null && mc.player.hasVehicle() && mc.player.input.sneaking
        && event.getPacket() instanceof PlayerPositionLookS2CPacket || event.getPacket() instanceof EntityPassengersSetS2CPacket) {
            event.setCancelled(true);
        }
    }
}

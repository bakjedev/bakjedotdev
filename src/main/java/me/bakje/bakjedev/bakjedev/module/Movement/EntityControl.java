package me.bakje.bakjedev.bakjedev.module.Movement;

import me.bakje.bakjedev.bakjedev.event.events.EntityControlEvent;
import me.bakje.bakjedev.bakjedev.event.events.PacketEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.Settings.BooleanSetting;
import me.bakje.bakjedev.bakjedev.module.Settings.NumberSetting;
import me.bakje.bakjedev.bakjedev.util.world.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemSteerable;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;

public class EntityControl extends Mod {
    public BooleanSetting entitySpeed = new BooleanSetting("EntitySpeed", true);
    public NumberSetting speed = new NumberSetting("Speed", 0, 5, 4.1, 0.1);
    public NumberSetting ascend = new NumberSetting("Ascend", 0, 2, 2, 0.3);
    public NumberSetting descend = new NumberSetting("Descend", 0, 2, 0.1, 0.1);
    public BooleanSetting fly = new BooleanSetting("Fly", false);
    public BooleanSetting antiStuck = new BooleanSetting("AntiStuck", true);
    public BooleanSetting noAi = new BooleanSetting("NoAI", true);
    public BooleanSetting antiDismount = new BooleanSetting("AntiDismount", false);


    public EntityControl() {
        super("EntityControl", "speed but animal", Category.MOVEMENT, true);
        addSettings(speed, fly, ascend, descend, antiStuck, noAi, antiDismount);
    }

    @Override
    public void onTick() {
        if (mc.player.getVehicle() ==null) return;

        Entity vehicle = mc.player.getVehicle();
        double speed = ModuleManager.INSTANCE.getModule(EntityControl.class).speed.getValue();

        double forward = mc.player.forwardSpeed;
        double strafe = mc.player.sidewaysSpeed;
        float yaw = mc.player.getYaw();

        vehicle.setYaw(yaw);
        if (vehicle instanceof LlamaEntity) {
            ((LlamaEntity) vehicle).headYaw = mc.player.headYaw;
        }

        if (this.noAi.isEnabled() && forward==0 && strafe==0) {
            vehicle.setVelocity(new Vec3d(0, vehicle.getVelocity().y,0));
        }

        if (this.entitySpeed.isEnabled()) {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }

                if (forward > 0.0D) {
                    forward = 1.0D;
                } else if (forward < 0.0D) {
                    forward = -1.0D;
                }

                strafe = 0.0D;
            }

            vehicle.setVelocity(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)),
                    vehicle.getVelocity().y,
                    forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));

        }

        if (this.fly.isEnabled()) {
            if (mc.options.jumpKey.isPressed()) {
                vehicle.setVelocity(vehicle.getVelocity().x, ascend.getValue(), vehicle.getVelocity().z);
            } else {
                vehicle.setVelocity(vehicle.getVelocity().x, -descend.getValue(), vehicle.getVelocity().z);
            }
        }

        if (this.antiStuck.isEnabled()) {
            Vec3d vel = vehicle.getVelocity().multiply(2);
            if (WorldUtil.doesBoxCollide(vehicle.getBoundingBox().offset(vel.x, 0, vel.z))) {
                for (int i = 2; i < 10; i++) {
                    if (!WorldUtil.doesBoxCollide(vehicle.getBoundingBox().offset(vel.x / i, 0, vel.z / i))) {
                        vehicle.setVelocity(vel.x / i / 2, vel.y, vel.z / i / 2);
                        break;
                    }
                }
            }
        }
        super.onTick();
    }

    @BakjeSubscribe
    public void onReadPacket(PacketEvent.Read event) {
        if (mc.player.getVehicle() ==null) return;
        if (this.antiDismount.isEnabled()) {
            if (mc.player != null && mc.player.hasVehicle() && mc.player.input.sneaking
                    && event.getPacket() instanceof PlayerPositionLookS2CPacket || event.getPacket() instanceof EntityPassengersSetS2CPacket) {
                event.setCancelled(true);
            }
        }
    }

    @BakjeSubscribe
    public void onEntityControl(EntityControlEvent event) {
        if (mc.player.getVehicle() ==null) return;
        if (mc.player.getVehicle() instanceof ItemSteerable && mc.player.forwardSpeed == 0 && mc.player.sidewaysSpeed == 0) {
            return;
        }

        event.setControllable(true);
    }
}

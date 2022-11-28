package me.bakje.bakjedev.bakjedev.module.movement;

import me.bakje.bakjedev.bakjedev.event.events.ClientMoveEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.settings.ModeSetting;
import me.bakje.bakjedev.bakjedev.module.settings.NumberSetting;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ElytraFly extends Mod {
    public ModeSetting eflyMode = new ModeSetting("Mode","Control", "Control", "Boost");

    public NumberSetting boostSpeed = new NumberSetting("BoostSpeed", 0, 0.15, 0.05, 0.01);
    public NumberSetting maxBoost = new NumberSetting("MaxBoost", 0, 5, 2.5, 0.1);
    public NumberSetting speed = new NumberSetting("Speed", 0, 5, 0.8, 0.01);
    public ElytraFly() {
        super("ElytraFly", "hacker flys with wing", Category.MOVEMENT, true);
        addSettings(eflyMode,boostSpeed, maxBoost, speed);
    }

    @Override
    public void onTick() {
        if (this.eflyMode.getIndex()==1) {
            double currentVel = Math.abs(mc.player.getVelocity().x) + Math.abs(mc.player.getVelocity().y) + Math.abs(mc.player.getVelocity().z);
            float radianYaw = (float) Math.toRadians(mc.player.getYaw());
            float boost = this.boostSpeed.getValueFloat();

            if (mc.player.isFallFlying() && currentVel <= this.maxBoost.getValue()) {
                if (mc.options.backKey.isPressed()) {
                    mc.player.addVelocity(MathHelper.sin(radianYaw) * boost, 0, MathHelper.cos(radianYaw) * -boost);
                } else if (mc.player.getPitch() > 0) {
                    mc.player.addVelocity(MathHelper.sin(radianYaw) * -boost, 0, MathHelper.cos(radianYaw) * boost);
                }
            }
        }

        if (this.eflyMode.getIndex()==0) {
            Vec3d vec3d = new Vec3d(0, 0, this.speed.getValue())
                    .rotateY(-(float) Math.toRadians(mc.player.getYaw()));


            if (mc.player.isFallFlying()) {
                if (mc.options.backKey.isPressed()) vec3d = vec3d.negate();
                if (mc.options.leftKey.isPressed()) vec3d = vec3d.rotateY((float) Math.toRadians(90));
                else if (mc.options.rightKey.isPressed()) vec3d = vec3d.rotateY(-(float) Math.toRadians(90));
                if (mc.options.jumpKey.isPressed()) vec3d = vec3d.add(0, this.speed.getValue(), 0);
                if (mc.options.sneakKey.isPressed()) vec3d = vec3d.add(0, -this.speed.getValue(), 0);

                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        mc.player.getX() + vec3d.x, mc.player.getY() - 0.01, mc.player.getZ() + vec3d.z, false));

                mc.player.setVelocity(vec3d.x, vec3d.y, vec3d.z);
            }
        }
        super.onTick();
    }

    @BakjeSubscribe
    public void onClientMove(ClientMoveEvent event) {
        if (this.eflyMode.getIndex()==0) {
            if (mc.player.isFallFlying()) {
                if (!mc.options.jumpKey.isPressed() && !mc.options.sneakKey.isPressed()) {
                    event.setVec(new Vec3d(event.getVec().x, 0, event.getVec().z));
                }

                if (!mc.options.backKey.isPressed() && !mc.options.leftKey.isPressed() && !mc.options.rightKey.isPressed() && !mc.options.forwardKey.isPressed()) {
                    event.setVec(new Vec3d(0, event.getVec().y, 0));
                }
            }
        }
    }

}

package me.bakje.bakjedev.bakjedev.module.Movement;

import me.bakje.bakjedev.bakjedev.event.events.ClientMoveEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.NumberSetting;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class ElytraControl extends Mod {
    public NumberSetting speed = new NumberSetting("Speed", 0, 5, 0.8, 0.01);


    public ElytraControl() {
        super("ElytraControl", "hacker", Category.MOVEMENT, true);
        addSetting(speed);
    }

    @BakjeSubscribe
    public void onClientMove(ClientMoveEvent event) {
        if (mc.player.isFallFlying()) {
            if (!mc.options.jumpKey.isPressed() && ! mc.options.sneakKey.isPressed()) {
                event.setVec(new Vec3d(event.getVec().x, 0, event.getVec().z));
            }

            if (!mc.options.backKey.isPressed() && ! mc.options.leftKey.isPressed() && !mc.options.rightKey.isPressed() && !mc.options.forwardKey.isPressed()) {
                event.setVec(new Vec3d(0, event.getVec().y, 0));
            }
        }
    }

    @Override
    public void onTick() {
        super.onTick();
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
}

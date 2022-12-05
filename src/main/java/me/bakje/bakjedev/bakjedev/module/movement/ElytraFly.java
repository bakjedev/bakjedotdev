package me.bakje.bakjedev.bakjedev.module.movement;

import me.bakje.bakjedev.bakjedev.event.events.ClientMoveEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.settings.BooleanSetting;
import me.bakje.bakjedev.bakjedev.module.settings.ModeSetting;
import me.bakje.bakjedev.bakjedev.module.settings.NumberSetting;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ElytraFly extends Mod {
    public ModeSetting eflyMode = new ModeSetting("Mode","Control", "Control", "Boost");
    public BooleanSetting autoOpen = new BooleanSetting("AutoOpen", true);
    public NumberSetting boostSpeed = new NumberSetting("BoostSpeed", 0, 0.15, 0.05, 0.01);
    public NumberSetting maxBoost = new NumberSetting("MaxBoost", 0, 5, 2.5, 0.1);
    public NumberSetting speed = new NumberSetting("Speed", 0.5, 3, 1.26, 0.01);
    public ElytraFly() {
        super("ElytraFly", "hacker flys with wing", Category.MOVEMENT, true);
        addSettings(eflyMode, speed, boostSpeed, maxBoost, autoOpen);
    }

    @BakjeSubscribe
    public void onClientMove(ClientMoveEvent event) {
        if (this.eflyMode.getIndex() == 0 && mc.player.isFallFlying()) {
            if (!mc.options.jumpKey.isPressed() && !mc.options.sneakKey.isPressed()) {
                event.setVec(new Vec3d(event.getVec().x, -0.0001, event.getVec().z));
            }

            if (!mc.options.backKey.isPressed() && !mc.options.leftKey.isPressed()
                    && !mc.options.rightKey.isPressed() && !mc.options.forwardKey.isPressed()) {
                event.setVec(new Vec3d(0, event.getVec().y-0.0001, 0));
            }
        }
    }

    @Override
    public void onTick() {
        assert mc.world != null;
        Vec3d vec3d;
        if (mc.world.getRegistryKey().getValue().getPath().equalsIgnoreCase("the_end")) {
            vec3d = new Vec3d(0, 0, this.speed.getValue()).rotateX(this.eflyMode.getIndex() == 0 ? 0 : -(float) Math.toRadians(mc.player.getPitch())).rotateY(-(float) Math.toRadians(mc.player.getYaw()));
        } else if (mc.world.getRegistryKey().getValue().getPath().equalsIgnoreCase("the_nether")) {
            vec3d = new Vec3d(0, 0, this.speed.getValue()).rotateX(this.eflyMode.getIndex() == 0 ? 0 : -(float) Math.toRadians(mc.player.getPitch())).rotateY(-(float) Math.toRadians(mc.player.getYaw()));
        } else {
            vec3d = new Vec3d(0, 0, this.speed.getValue()).rotateX(this.eflyMode.getIndex() == 0 ? 0 : -(float) Math.toRadians(mc.player.getPitch())).rotateY(-(float) Math.toRadians(mc.player.getYaw()));
        }
        if (!mc.player.isFallFlying() && !mc.player.isOnGround() && this.eflyMode.getIndex() == 0 && mc.player.age % 10 == 0 && this.autoOpen.isEnabled()) {
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        }
        if (mc.player.isFallFlying()) {
            if (this.eflyMode.getIndex() == 0) {
                if (mc.options.backKey.isPressed()) vec3d = vec3d.multiply(-1);
                if (mc.options.leftKey.isPressed()) vec3d = vec3d.rotateY((float) Math.toRadians(90));
                if (mc.options.rightKey.isPressed()) vec3d = vec3d.rotateY(-(float) Math.toRadians(90));

                if (mc.options.jumpKey.isPressed()) vec3d = vec3d.add(0, this.speed.getValue(), 0);
                if (mc.options.sneakKey.isPressed()) vec3d = vec3d.add(0, -this.speed.getValue(), 0);

                if (!mc.options.backKey.isPressed() && !mc.options.leftKey.isPressed()
                        && !mc.options.rightKey.isPressed() && !mc.options.forwardKey.isPressed()
                        && !mc.options.jumpKey.isPressed() && !mc.options.sneakKey.isPressed()) vec3d = Vec3d.ZERO;
                mc.player.setVelocity(vec3d.multiply(2));
            }
        }
    }
}
package me.bakje.bakjedev.bakjedev.module.movement;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.settings.BooleanSetting;
import me.bakje.bakjedev.bakjedev.module.settings.ModeSetting;
import me.bakje.bakjedev.bakjedev.module.settings.NumberSetting;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Flight extends Mod {

    public ModeSetting flightMode = new ModeSetting("Mode","Vanilla", "Vanilla", "Static");
    public NumberSetting speed = new NumberSetting("Speed", 0, 5, 1, 0.1);
    public BooleanSetting antiKick = new BooleanSetting("Anti kick", false);
    public static ClientPlayNetworkHandler networkHandler;
    int antiKickCounter = 0;

    public Flight() {
        super("Flight", "flying like bruh", Category.MOVEMENT, true);
        addSettings(flightMode, speed, antiKick);
    }


    @Override
    public void onTick() {
        if (flightMode.getIndex()==0) {
            if (antiKick.isEnabled()) {
                antiKickCounter++;
                if (antiKickCounter > 20 && mc.player.world.getBlockState(new BlockPos(mc.player.getPos().subtract(0,0.0433D,0))).isAir()) {
                    antiKickCounter = 0;
                    mc.player.setPos(mc.player.getX(), mc.player.getY()-0.0433D, mc.player.getZ());
                }
            }
            mc.player.getAbilities().flying = true;
            mc.player.getAbilities().setFlySpeed((float) speed.getValueFloat());

        }


        if (this.flightMode.isMode("Static")) {
            Vec3d antiKickVel = Vec3d.ZERO;

            mc.player.setVelocity(antiKickVel);

            Vec3d forward = new Vec3d(0, 0, this.speed.getValue()).rotateY(-(float) Math.toRadians(mc.player.getYaw()));
            Vec3d strafe = forward.rotateY((float) Math.toRadians(90));

            if (mc.options.jumpKey.isPressed())
                mc.player.setVelocity(mc.player.getVelocity().add(0, this.speed.getValue(), 0));
            if (mc.options.sneakKey.isPressed())
                mc.player.setVelocity(mc.player.getVelocity().add(0, -this.speed.getValue(), 0));
            if (mc.options.backKey.isPressed())
                mc.player.setVelocity(mc.player.getVelocity().add(-forward.x, 0, -forward.z));
            if (mc.options.forwardKey.isPressed())
                mc.player.setVelocity(mc.player.getVelocity().add(forward.x, 0, forward.z));
            if (mc.options.leftKey.isPressed())
                mc.player.setVelocity(mc.player.getVelocity().add(strafe.x, 0, strafe.z));
            if (mc.options.rightKey.isPressed())
                mc.player.setVelocity(mc.player.getVelocity().add(-strafe.x, 0, -strafe.z));

        }
        super.onTick();
    }
    @Override
    public void onDisable() {
        mc.player.getAbilities().flying = false;
        super.onDisable();
    }
}

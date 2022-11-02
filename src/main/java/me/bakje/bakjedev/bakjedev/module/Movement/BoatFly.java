package me.bakje.bakjedev.bakjedev.module.Movement;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.BooleanSetting;
import me.bakje.bakjedev.bakjedev.module.Settings.ModeSetting;
import me.bakje.bakjedev.bakjedev.module.Settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class BoatFly extends Mod {
    public NumberSetting speed = new NumberSetting("Speed", 0, 10, 1, 0.1);
    public BoatFly() {
        super("Boatfly", "boat flying like bruh", Category.MOVEMENT);
        addSetting(speed);
    }

    @Override
    public void onTick() {
        if (mc.player.hasVehicle()) {
            Entity vehicle = mc.player.getVehicle();
            Vec3d velocity = vehicle.getVelocity();
            double motionY = mc.options.jumpKey.isPressed() ? 0.3 : 0;
            vehicle.setVelocity(new Vec3d(velocity.x, motionY, velocity.z));
        }
        super.onTick();
    }
}

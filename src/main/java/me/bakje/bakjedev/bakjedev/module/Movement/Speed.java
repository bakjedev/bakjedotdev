package me.bakje.bakjedev.bakjedev.module.Movement;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.ModeSetting;
import org.lwjgl.glfw.GLFW;

public class Speed extends Mod {

    public ModeSetting speedMode = new ModeSetting("Mode","Boost", "Boost", "Speed2");
    public Speed() {
        super("Speed", "fast fast", Category.MOVEMENT);
        addSetting(speedMode);
    }

    @Override
    public void onTick() {
        if (speedMode.getMode().equalsIgnoreCase("Boost")) {
            mc.player.setVelocity(mc.player.getVelocity().x * 1.1, mc.player.getVelocity().y, mc.player.getVelocity().z * 1.1);
        }
        super.onTick();
    }
}

package me.bakje.bakjedev.bakjedev.module.Movement;

import me.bakje.bakjedev.bakjedev.module.Mod;
import org.lwjgl.glfw.GLFW;

public class Sprint extends Mod {
    public Sprint() {
        super("Sprint", "run run run", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        mc.player.setSprinting(true);
        super.onTick();
    }
}

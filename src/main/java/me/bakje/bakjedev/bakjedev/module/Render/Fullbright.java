package me.bakje.bakjedev.bakjedev.module.Render;

import me.bakje.bakjedev.bakjedev.module.Mod;

public class Fullbright extends Mod {

    public Fullbright() {
        super("Fullbright", "bright up your world", Category.RENDER);
    }

    @Override
    public void onEnable() {
        mc.worldRenderer.reload();
        super.onEnable();
    }
    @Override
    public void onDisable() {
        mc.worldRenderer.reload();
        super.onDisable();
    }
}

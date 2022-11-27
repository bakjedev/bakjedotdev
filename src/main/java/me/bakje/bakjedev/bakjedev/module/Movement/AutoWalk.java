package me.bakje.bakjedev.bakjedev.module.Movement;

import me.bakje.bakjedev.bakjedev.module.Mod;

public class AutoWalk extends Mod {
    public AutoWalk() {
        super("AutoWalk", "cant press w?", Category.MOVEMENT, true);
    }

    @Override
    public void onTick() {
        mc.options.forwardKey.setPressed(true);
        super.onTick();
    }

    @Override
    public void onDisable() {
        mc.options.forwardKey.setPressed(false);
        super.onDisable();
    }
}

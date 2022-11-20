package me.bakje.bakjedev.bakjedev.module.Movement;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.NumberSetting;
import net.minecraft.util.math.MathHelper;

public class Timer extends Mod {
    public NumberSetting speed = new NumberSetting("Speed", 0.1, 10, 1, 0.1);

    public Timer() {
        super("Timer", "time travel machine", Category.MOVEMENT, true);
        addSetting(speed);
    }
}

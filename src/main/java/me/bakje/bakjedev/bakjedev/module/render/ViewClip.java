package me.bakje.bakjedev.bakjedev.module.render;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.settings.BooleanSetting;
import me.bakje.bakjedev.bakjedev.module.settings.NumberSetting;

public class ViewClip extends Mod {
    public BooleanSetting CameraClip = new BooleanSetting("CameraClip", true);
    public BooleanSetting DistanceToggle = new BooleanSetting("CustomDistance", true);
    public NumberSetting Distance = new NumberSetting("Distance", 0.5, 15, 4, 1);
    public ViewClip() {
        super("ViewClip", "clip walls with cam in f5", Category.RENDER, true);
        addSettings(CameraClip, Distance, DistanceToggle);
    }

    //see mixin
}

package me.bakje.bakjedev.bakjedev.module.Render;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.BooleanSetting;
import me.bakje.bakjedev.bakjedev.module.Settings.NumberSetting;

public class ViewClip extends Mod {
    public BooleanSetting CameraClip = new BooleanSetting("CameraClip", true);
    public BooleanSetting DistanceToggle = new BooleanSetting("CustomDistance", true);
    public NumberSetting Distance = new NumberSetting("Distance", 0.5, 15, 4, 1);
    public ViewClip() {
        super("ViewClip", "clip walls with cam in f5", Category.RENDER);
        addSettings(CameraClip, Distance, DistanceToggle);
        //2
    }

    //see mixin
}

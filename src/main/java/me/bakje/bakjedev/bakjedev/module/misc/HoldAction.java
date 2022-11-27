package me.bakje.bakjedev.bakjedev.module.misc;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.settings.ModeSetting;


public class HoldAction extends Mod {
    public ModeSetting actionMode = new ModeSetting("Mode","Use", "Use", "Attack");
    public HoldAction() {
        super("HoldAction", "continuous breaking or using", Category.MISC, true);
        addSetting(actionMode);
    }

    //mixin time
}
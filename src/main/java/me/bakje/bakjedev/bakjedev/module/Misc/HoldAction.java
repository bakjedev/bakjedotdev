package me.bakje.bakjedev.bakjedev.module.Misc;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.ModeSetting;


public class HoldAction extends Mod {
    public ModeSetting actionMode = new ModeSetting("Mode","Use", "Use", "Attack");
    public HoldAction() {
        super("HoldAction", "continuous breaking or using", Category.MISC, true);
        addSetting(actionMode);
    }

    //mixin time
    //ModuleManager.INSTANCE.getModule(Flight.class).flightMode
}
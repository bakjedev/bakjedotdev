package me.bakje.bakjedev.bakjedev.module;

import me.bakje.bakjedev.bakjedev.module.Combat.Aura;
import me.bakje.bakjedev.bakjedev.module.Exploit.WGBypass;
import me.bakje.bakjedev.bakjedev.module.Misc.*;
import me.bakje.bakjedev.bakjedev.module.Movement.*;
import me.bakje.bakjedev.bakjedev.module.Render.Fullbright;
import me.bakje.bakjedev.bakjedev.module.Render.Xray;


import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static final ModuleManager INSTANCE = new ModuleManager();
    private List<Mod> modules = new ArrayList<>();

    public ModuleManager() {
        addModules();
    }

    public List<Mod> getModules() {
        return modules;
    }

    public List<Mod> getEnabledModules() {
        List<Mod> enabled = new ArrayList<>();
        for (Mod module : modules) {
            if(module.isEnabled()) enabled.add(module);
        }
        return enabled;
    }

    public List<Mod> getModulesInCategory(Mod.Category category) {
        List<Mod> categoryModules = new ArrayList<>();

        for (Mod mod : modules) {
            if (mod.getCategory() == category) {
                categoryModules.add(mod);
            }
        }
        return categoryModules;
    }

    public Mod isModEnabled(String s) {
        for(Mod m : modules) {
            if(m.getName().equalsIgnoreCase(s))
                return m;
        }
        return null;
    }


    private void addModules() {
        modules.add(new Flight());
        modules.add(new Sprint());
        modules.add(new Speed());
        modules.add(new BoatFly());
        modules.add(new Nofall());

        modules.add(new Fullbright());
        modules.add(new Xray());

        modules.add(new Spin());
        modules.add(new LOHarvest());
        modules.add(new PortalGUI());
        modules.add(new look());

        modules.add(new Aura());

        modules.add(new WGBypass());
    }


}

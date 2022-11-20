package me.bakje.bakjedev.bakjedev.module;

import me.bakje.bakjedev.bakjedev.module.Combat.*;
import me.bakje.bakjedev.bakjedev.module.Misc.*;
import me.bakje.bakjedev.bakjedev.module.Movement.*;
import me.bakje.bakjedev.bakjedev.module.Render.*;
import me.bakje.bakjedev.bakjedev.module.World.Scaffold;
import me.bakje.bakjedev.bakjedev.module.Exploit.WGBypass;


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

      @SuppressWarnings("unchecked")
    public <T extends Mod> T getModule(Class<T> clazz) {
        return (T) modules.stream().filter(mod -> mod.getClass() == clazz).findFirst().orElse(null);
    }

    private void addModules() {
        modules.add(new Flight());
        modules.add(new ElytraBoost());
        modules.add(new Sprint());
        modules.add(new Speed());
        modules.add(new AntiHunger());
        modules.add(new BoatFly());
        modules.add(new Nofall());
        modules.add(new FakeSneak());
        modules.add(new NoSlow());
        modules.add(new GUIMove());

        modules.add(new Nametags());
        modules.add(new NoRender());
        modules.add(new Fullbright());
        modules.add(new Xray());
        modules.add(new HudModule());
        modules.add(new ViewClip());

        modules.add(new HoldAction());
        modules.add(new ChatTimestamps());
        modules.add(new ChestSwap());
        modules.add(new Peek());
        modules.add(new PortalGUI());
        modules.add(new AutoFish());
        modules.add(new PartyChat());
        modules.add(new LOHarvest());
        modules.add(new config());

        modules.add(new Aura());
        modules.add(new Surround());
        modules.add(new Velocity());
        modules.add(new AutoTotem());
        modules.add(new Auto32K());
        modules.add(new Grab32k());

        modules.add(new WGBypass());

        modules.add(new Scaffold());
    }


}

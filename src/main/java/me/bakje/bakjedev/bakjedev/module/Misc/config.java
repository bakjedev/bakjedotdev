package me.bakje.bakjedev.bakjedev.module.Misc;

import me.bakje.bakjedev.bakjedev.module.Combat.Aura;
import me.bakje.bakjedev.bakjedev.module.Combat.AutoTotem;
import me.bakje.bakjedev.bakjedev.module.Combat.RussianSurround;
import me.bakje.bakjedev.bakjedev.module.Combat.Velocity;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.Movement.Flight;
import me.bakje.bakjedev.bakjedev.module.Render.*;


public class config extends Mod {
    public config() {
        super("config", "bakje config", Category.MISC);
    }
    @Override
    public void onEnable() {
        ModuleManager.INSTANCE.getModule(CleanView.class).toggle();
        ModuleManager.INSTANCE.getModule(Fullbright.class).toggle();
        ModuleManager.INSTANCE.getModule(NoBossbar.class).toggle();
        ModuleManager.INSTANCE.getModule(NoFog.class).toggle();
        ModuleManager.INSTANCE.getModule(NoVanillaEffectHUD.class).toggle();
        ModuleManager.INSTANCE.getModule(ChatTimestamps.class).toggle();
        ModuleManager.INSTANCE.getModule(PortalGUI.class).toggle();
        ModuleManager.INSTANCE.getModule(AutoTotem.class).toggle();
        ModuleManager.INSTANCE.getModule(Velocity.class).toggle();

        ModuleManager.INSTANCE.getModule(Aura.class).setKey(71);
        ModuleManager.INSTANCE.getModule(RussianSurround.class).setKey(70);
        ModuleManager.INSTANCE.getModule(Flight.class).setKey(86);
        ModuleManager.INSTANCE.getModule(Xray.class).setKey(88);
        ModuleManager.INSTANCE.getModule(ChestSwap.class).setKey(90);

        ModuleManager.INSTANCE.getModule(Aura.class).rotate.setEnabled(true);
        ModuleManager.INSTANCE.getModule(Flight.class).antiKick.setEnabled(true);
        ModuleManager.INSTANCE.getModule(Flight.class).speed.setValue(0.1);
        this.toggle();
        super.onEnable();
    }
}
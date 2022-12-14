package me.bakje.bakjedev.bakjedev.module.misc;

import me.bakje.bakjedev.bakjedev.module.combat.*;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.movement.*;
import me.bakje.bakjedev.bakjedev.module.render.*;
import me.bakje.bakjedev.bakjedev.module.world.Scaffold;


public class Config extends Mod {
    public Config() {
        super("Config", "bakje config", Category.MISC, true);
    }
    @Override
    public void onEnable() {
        ModuleManager.INSTANCE.getModule(HudModule.class).toggle();
        ModuleManager.INSTANCE.getModule(Fullbright.class).toggle();
        ModuleManager.INSTANCE.getModule(ViewClip.class).toggle();
        ModuleManager.INSTANCE.getModule(NoRender.class).toggle();
        ModuleManager.INSTANCE.getModule(ChatTimestamps.class).toggle();
        ModuleManager.INSTANCE.getModule(PortalGUI.class).toggle();
        ModuleManager.INSTANCE.getModule(AutoTotem.class).toggle();
        ModuleManager.INSTANCE.getModule(Velocity.class).toggle();
        ModuleManager.INSTANCE.getModule(NoSlow.class).toggle();
        ModuleManager.INSTANCE.getModule(AntiHunger.class).toggle();
        ModuleManager.INSTANCE.getModule(Nametags.class).toggle();
        ModuleManager.INSTANCE.getModule(PopIdentifier.class).toggle();
        ModuleManager.INSTANCE.getModule(Peek.class).toggle();
        ModuleManager.INSTANCE.getModule(HoleESP.class).toggle();
        ModuleManager.INSTANCE.getModule(MCF.class).toggle();
        ModuleManager.INSTANCE.getModule(ElytraBoost.class).toggle();

        ModuleManager.INSTANCE.getModule(Aura.class).setKey(71);
        ModuleManager.INSTANCE.getModule(Surround.class).setKey(70);
        ModuleManager.INSTANCE.getModule(Flight.class).setKey(78);
        ModuleManager.INSTANCE.getModule(Speed.class).setKey(96);
        ModuleManager.INSTANCE.getModule(Freecam.class).setKey(88);
        ModuleManager.INSTANCE.getModule(Xray.class).setKey(74);
        ModuleManager.INSTANCE.getModule(ChestSwap.class).setKey(90);
        ModuleManager.INSTANCE.getModule(AutoCrystal.class).setKey(89);
        ModuleManager.INSTANCE.getModule(Scaffold.class).setKey(85);
        ModuleManager.INSTANCE.getModule(ElytraFly.class).setKey(66);

        ModuleManager.INSTANCE.getModule(Aura.class).rotate.setEnabled(true);
        ModuleManager.INSTANCE.getModule(Flight.class).antiKick.setEnabled(true);
        ModuleManager.INSTANCE.getModule(Flight.class).speed.setValue(2.5);
        ModuleManager.INSTANCE.getModule(Flight.class).flightMode.setIndex(1);
        ModuleManager.INSTANCE.getModule(Peek.class).info.setIndex(1);
        ModuleManager.INSTANCE.getModule(Speed.class).strafeJumping.setEnabled(true);
        ModuleManager.INSTANCE.getModule(HudModule.class).arraylistRainbow.setIndex(0);
        ModuleManager.INSTANCE.getModule(Timer.class).speed.setValue(1.1);
        ModuleManager.INSTANCE.getModule(HudModule.class).theme.setIndex(5);
        this.toggle();
        super.onEnable();
    }
}
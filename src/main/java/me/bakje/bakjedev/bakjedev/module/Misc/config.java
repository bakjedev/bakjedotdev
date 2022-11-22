package me.bakje.bakjedev.bakjedev.module.Misc;

import me.bakje.bakjedev.bakjedev.module.Combat.Aura;
import me.bakje.bakjedev.bakjedev.module.Combat.AutoTotem;
import me.bakje.bakjedev.bakjedev.module.Combat.Surround;
import me.bakje.bakjedev.bakjedev.module.Combat.Velocity;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.Movement.*;
import me.bakje.bakjedev.bakjedev.module.Render.*;


public class config extends Mod {
    public config() {
        super("Config", "bakje config", Category.MISC, true);
    }
    @Override
    public void onEnable() {
        ModuleManager.INSTANCE.getModule(Fullbright.class).toggle();
        ModuleManager.INSTANCE.getModule(ViewClip.class).toggle();
        ModuleManager.INSTANCE.getModule(NoRender.class).toggle();
        ModuleManager.INSTANCE.getModule(ChatTimestamps.class).toggle();
        ModuleManager.INSTANCE.getModule(PortalGUI.class).toggle();
        ModuleManager.INSTANCE.getModule(AutoTotem.class).toggle();
        ModuleManager.INSTANCE.getModule(Velocity.class).toggle();
        ModuleManager.INSTANCE.getModule(NoSlow.class).toggle();
        ModuleManager.INSTANCE.getModule(GUIMove.class).toggle();
        ModuleManager.INSTANCE.getModule(AntiHunger.class).toggle();
        ModuleManager.INSTANCE.getModule(Nametags.class).toggle();


        ModuleManager.INSTANCE.getModule(Aura.class).setKey(71);
        ModuleManager.INSTANCE.getModule(Surround.class).setKey(70);
        ModuleManager.INSTANCE.getModule(Flight.class).setKey(86);
        ModuleManager.INSTANCE.getModule(Speed.class).setKey(96);
        ModuleManager.INSTANCE.getModule(Freecam.class).setKey(88);
        ModuleManager.INSTANCE.getModule(Xray.class).setKey(74);
        ModuleManager.INSTANCE.getModule(ChestSwap.class).setKey(90);
        ModuleManager.INSTANCE.getModule(PartyChat.class).setKey(89);
        ModuleManager.INSTANCE.getModule(Peek.class).setKey(76);

        ModuleManager.INSTANCE.getModule(Aura.class).rotate.setEnabled(true);
        ModuleManager.INSTANCE.getModule(Aura.class).attackEverything.setEnabled(true);
        ModuleManager.INSTANCE.getModule(Flight.class).antiKick.setEnabled(true);
        ModuleManager.INSTANCE.getModule(Flight.class).speed.setValue(0.1);
        ModuleManager.INSTANCE.getModule(Speed.class).strafeJumping.setEnabled(true);
        ModuleManager.INSTANCE.getModule(HudModule.class).arraylistRainbow.setMode("V");
        this.toggle();
        super.onEnable();
    }
}
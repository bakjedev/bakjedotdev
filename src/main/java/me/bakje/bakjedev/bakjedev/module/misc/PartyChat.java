package me.bakje.bakjedev.bakjedev.module.misc;

import me.bakje.bakjedev.bakjedev.module.Mod;


public class PartyChat extends Mod {
    public PartyChat() {
        super("PartyChat", "for constantiam lol", Category.MISC, true);
    }
    @Override
    public void onEnable() {
        mc.player.networkHandler.sendCommand("party chat on");
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.player.networkHandler.sendCommand("party chat off");
        super.onDisable();
    }

}
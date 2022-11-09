package me.bakje.bakjedev.bakjedev.module.Render;

import me.bakje.bakjedev.bakjedev.event.events.PacketEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.BooleanSetting;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.MathHelper;

public class HudModule extends Mod {
    public long lastPacket = 0;
    private long prevTime = 0;
    public double tps = 0;
    public BooleanSetting info = new BooleanSetting("Info", true);
    public BooleanSetting arraylist = new BooleanSetting("Arraylist", true);
    public BooleanSetting coords = new BooleanSetting("Coords", true);
    public BooleanSetting dir = new BooleanSetting("Direction", true);
    public HudModule() {
        super("Hud", "shows info", Category.RENDER, true);
        addSettings(info, arraylist, coords, dir);
    }

    @BakjeSubscribe
    public void onReadPacket(PacketEvent.Read event) {
        lastPacket= System.currentTimeMillis();

        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            long time = System.currentTimeMillis();
            long timeOffset = Math.abs(1000-(time-prevTime)) +1000;
            tps = (MathHelper.clamp(20 / (timeOffset /1000d), 0, 20) * 100d)/100d;
            prevTime = time;
        }
    }

}

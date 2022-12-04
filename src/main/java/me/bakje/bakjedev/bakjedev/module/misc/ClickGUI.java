package me.bakje.bakjedev.bakjedev.module.misc;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.settings.ModeSetting;
import me.bakje.bakjedev.bakjedev.module.settings.NumberSetting;
import net.minecraft.text.Text;


public class ClickGUI extends Mod {
    public ModeSetting mainColor = new ModeSetting("MainColor","", "");
    public NumberSetting mred = new NumberSetting("Red", 1, 255, 85, 1);
    public NumberSetting mgreen = new NumberSetting("Green", 1, 255, 85, 1);
    public NumberSetting mblue = new NumberSetting("Blue", 1, 255, 255, 1);
    public ModeSetting secondaryColor = new ModeSetting("SettingColor","", "");
    public NumberSetting secred = new NumberSetting("Red", 1, 255, 45, 1);
    public NumberSetting secgreen = new NumberSetting("Green", 1, 255, 45, 1);
    public NumberSetting secblue = new NumberSetting("Blue", 1, 255, 255, 1);
    public ModeSetting settingColor = new ModeSetting("SettingColor","", "");
    public NumberSetting setred = new NumberSetting("Red", 1, 255, 85, 1);
    public NumberSetting setgreen = new NumberSetting("Green", 1, 255, 85, 1);
    public NumberSetting setblue = new NumberSetting("Blue", 1, 255, 160, 1);

    public ClickGUI() {
        super("ClickGUI", "clickgui settings", Category.MISC, true);
        addSettings(mainColor,mred,mgreen,mblue,secondaryColor, secred,secgreen,secblue, settingColor,setred,setgreen,setblue);
    }

    @Override
    public void onEnable() {
        this.toggle();
        super.onEnable();
    }
}
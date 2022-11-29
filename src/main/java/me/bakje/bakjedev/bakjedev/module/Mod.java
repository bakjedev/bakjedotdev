package me.bakje.bakjedev.bakjedev.module;


import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.module.settings.Setting;
import me.bakje.bakjedev.bakjedev.module.settings.KeyBindSetting;
import me.bakje.bakjedev.bakjedev.ui.screens.clickgui.setting.KeyBind;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public abstract class Mod {

    private String name;
    private String displayName;
    private String description;
    private Category category;
    private int key;
    private boolean enabled;
    private boolean visible;

    private List<Setting> settings = new ArrayList<>();

    protected static MinecraftClient mc = MinecraftClient.getInstance();

    public Mod(String name, String description, Category category, boolean visible) {
        this.name = name;
        this.displayName = name;
        this.description = description;
        this.category = category;
        this.visible =visible;
        addSettings(new KeyBindSetting("Key", 0));
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public void addSetting(Setting setting) {
        settings.add(setting);
    }

    public void addSettings(Setting...settings) {
        for (Setting setting : settings) addSetting(setting);
    }

    public void toggle() {
        this.enabled = !this.enabled;

        if(enabled) onEnable(); else onDisable();
    }

    public void onEnable() {
        Bakjedev.INSTANCE.eventBus.subscribe(this);
    }

    public void onDisable() {
        Bakjedev.INSTANCE.eventBus.unsubscribe(this);
    }

    public void onTick() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isEnabled() {
        return enabled;
    }


    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if(enabled) onEnable(); else onDisable();
    }


    public enum Category {
        COMBAT("Combat"),
        MOVEMENT("Movement"),
        RENDER("Render"),
        EXPLOIT("Exploit"),
        WORLD("World"),
        MISC("Misc");

        public String name;
        private Category(String name) {
            this.name = name;
        }
    }
}


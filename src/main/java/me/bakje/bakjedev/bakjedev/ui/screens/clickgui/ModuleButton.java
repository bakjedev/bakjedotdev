package me.bakje.bakjedev.bakjedev.ui.screens.clickgui;

import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.misc.ClickGUI;
import me.bakje.bakjedev.bakjedev.ui.screens.clickgui.setting.KeyBind;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.ui.screens.clickgui.setting.Component;
import me.bakje.bakjedev.bakjedev.module.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleButton{
    public Mod module;
    public Frame parent;
    public int offset;
    public List<Component> components;
    public boolean extended;
    public ModuleButton(Mod module, Frame parent, int offset) {
        this.module = module;
        this.parent = parent;
        this.offset = offset;
        this.extended = false;
        this.components = new ArrayList<>();

        int setOffset = parent.height;
        for (Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                components.add(new CheckBox(setting, this, setOffset));
            } else if (setting instanceof ModeSetting) {
                components.add(new ModeBox(setting, this, setOffset));
            } else if (setting instanceof NumberSetting) {
                components.add(new Slider(setting, this, setOffset));
            } else if (setting instanceof KeyBindSetting) {
                components.add(new KeyBind(setting, this, setOffset));
            }
            setOffset += parent.height;
        }
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY,float delta){
        MinecraftClient mc = MinecraftClient.getInstance();
        ClickGUI clickGUI = ModuleManager.INSTANCE.getModule(ClickGUI.class);
        DrawableHelper.fill(matrices, parent.x, parent.y+ offset, parent.x + parent.width, parent.y+offset+parent.height, new Color(clickGUI.mred.getValueInt(), clickGUI.mgreen.getValueInt(), clickGUI.mblue.getValueInt(), 160).getRGB());
        if (isHovered(mouseX,mouseY)){
            DrawableHelper.fill(matrices, mc.getWindow().getWidth()/2-mc.textRenderer.getWidth(module.getDescription())-15, 14, mc.getWindow().getWidth()/2, 25+mc.textRenderer.fontHeight,new Color(clickGUI.mred.getValueInt(), clickGUI.mgreen.getValueInt(), clickGUI.mblue.getValueInt(), 160).getRGB());
            mc.textRenderer.drawWithShadow(matrices, module.getDescription(), mc.getWindow().getWidth()/2-mc.textRenderer.getWidth(module.getDescription())-10, 20, Color.WHITE.getRGB());
            DrawableHelper.fill(matrices, parent.x, parent.y+ offset, parent.x + parent.width, parent.y+offset+parent.height, new Color(clickGUI.mred.getValueInt(), clickGUI.mgreen.getValueInt(), clickGUI.mblue.getValueInt(), 160).getRGB());
        }

        int textOffset =((parent.height / 2) - parent.mc.textRenderer.fontHeight / 2);

        parent.mc.textRenderer.drawWithShadow(matrices, module.getName(), parent.x+textOffset,parent.y+offset+textOffset, module.isEnabled() ? new Color(60, 255, 255).getRGB() : -1);
        parent.mc.textRenderer.drawWithShadow(matrices, extended ? "-" : "+", parent.x + parent.width - 8 - parent.mc.textRenderer.getWidth("+"), parent.y + offset + 6, -1);
        if (extended) {
            for (Component component : components) {
                component.render(matrices, mouseX, mouseY, delta);
            }
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            if (button == 0) {
                module.toggle();
            } else if (button == 1) {
                extended = !extended;
                parent.updateButtons();
            } else if (button == 2) {
                module.setVisible(!module.isVisible());
            }
        }

        for (Component component : components) {
            component.mouseClicked(mouseX, mouseY, button);
        }

    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        for (Component component : components) {
            component.mouseReleased(mouseX, mouseY, button);
        }
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > parent.x && mouseX < parent.x+ parent.width && mouseY > parent.y +offset && mouseY < parent.y + offset + parent.height;
    }
    public void keyPressed(int key) {
        for (Component component : components) {
            component.keyPressed(key);
        }
    }
}

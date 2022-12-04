package me.bakje.bakjedev.bakjedev.ui.screens.clickgui.setting;

import java.awt.Color;

import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.misc.ClickGUI;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import me.bakje.bakjedev.bakjedev.module.settings.KeyBindSetting;
import me.bakje.bakjedev.bakjedev.module.settings.Setting;
import me.bakje.bakjedev.bakjedev.ui.screens.clickgui.ModuleButton;

public class KeyBind extends Component {

    private KeyBindSetting binding = (KeyBindSetting)setting;
    public boolean isBinding = false;

    public KeyBind(Setting setting, ModuleButton parent, int offset) {
        super(setting, parent, offset);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY) && button == 0 && parent.extended) {
            binding.toggle();
            isBinding = true;
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void keyPressed(int key) {
        if (isBinding) {
            parent.module.setKey(key);
            binding.setKey(key);
            isBinding = false;
        }
        if ((binding.getKey() == 256)) {
            parent.module.setKey(0);
            binding.setKey(0);
            isBinding = false;
        }
        if ((binding.getKey() == 259)) {
            parent.module.setKey(0);
            binding.setKey(0);
            isBinding = false;
        }
        super.keyPressed(key);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        ClickGUI clickGUI = ModuleManager.INSTANCE.getModule(ClickGUI.class);
        DrawableHelper.fill(matrices, parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset + offset + parent.parent.height, new Color(clickGUI.setred.getValueInt(),clickGUI.setgreen.getValueInt(),clickGUI.setblue.getValueInt(),160).getRGB());

        int offsetY = ((parent.parent.height / 2) - mc.textRenderer.fontHeight / 2);
        if (binding.getKey()==0) {
            if (!isBinding) mc.textRenderer.drawWithShadow(matrices, "Keybind: " + "none", parent.parent.x + offsetY, parent.parent.y + parent.offset + offset + offsetY, -1);
        } else {
            if (!isBinding) mc.textRenderer.drawWithShadow(matrices, "Keybind: " + (char)binding.getKey(), parent.parent.x + offsetY, parent.parent.y + parent.offset + offset + offsetY, -1);
        }
        if (isBinding) mc.textRenderer.drawWithShadow(matrices, "Binding...", parent.parent.x + offsetY, parent.parent.y + parent.offset + offset + offsetY, -1);


        super.render(matrices, mouseX, mouseY, delta);
    }
}
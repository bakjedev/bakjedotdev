package me.bakje.bakjedev.bakjedev.module.settings;

import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.misc.ClickGUI;
import me.bakje.bakjedev.bakjedev.ui.screens.clickgui.ModuleButton;
import me.bakje.bakjedev.bakjedev.ui.screens.clickgui.setting.Component;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class CheckBox extends Component {

    private BooleanSetting boolSet = (BooleanSetting)setting;

    public CheckBox(Setting setting, ModuleButton parent, int offset) {
        super(setting, parent, offset);
        this.boolSet = (BooleanSetting)setting;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        ClickGUI clickGUI = ModuleManager.INSTANCE.getModule(ClickGUI.class);
        DrawableHelper.fill(matrices, parent.parent.x, parent.parent.y+ parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y+parent.offset+offset+parent.parent.height, new Color(clickGUI.setred.getValueInt(),clickGUI.setgreen.getValueInt(),clickGUI.setblue.getValueInt(),160).getRGB());
        int textOffset =((parent.parent.height / 2) - mc.textRenderer.fontHeight / 2);
        mc.textRenderer.drawWithShadow(matrices, boolSet.getName() + ": " + boolSet.isEnabled(), parent.parent.x + textOffset, parent.parent.y + parent.offset + offset + textOffset, -1);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY) && button==0 && parent.extended) {
            boolSet.toggle();
        }
        super.mouseClicked(mouseX, mouseY, button);
    }


}

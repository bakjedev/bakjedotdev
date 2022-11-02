package me.bakje.bakjedev.bakjedev.UI.Screens.clickgui;

import me.bakje.bakjedev.bakjedev.module.Misc.PortalGUI;
import me.bakje.bakjedev.bakjedev.module.Mod.Category;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class ClickGui extends Screen {
    public static final ClickGui INSTANCE = new ClickGui();

    private List<Frame> frames;

    private ClickGui() {
        super(Text.literal("Click GUI"));

        frames = new ArrayList<>();

        int offset = 20;
        for (Category category : Category.values()) {
            frames.add(new Frame(category, offset, 20, 100, 20));
            offset += 120;
        }
    }


    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for (Frame frames : frames) {
            frames.render(matrices, mouseX, mouseY, delta);
            frames.updatePos(mouseX, mouseY);
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Frame frames : frames) {
            frames.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (Frame frames : frames) {
            frames.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Frame frame : frames) {
            frame.keyPressed(keyCode);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

}

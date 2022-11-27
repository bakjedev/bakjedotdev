package me.bakje.bakjedev.bakjedev.util;

import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.text.Text;

public class PeekShulkerScreen extends ShulkerBoxScreen {
    public PeekShulkerScreen(ShulkerBoxScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }
}

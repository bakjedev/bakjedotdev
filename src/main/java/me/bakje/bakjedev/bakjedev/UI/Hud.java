package me.bakje.bakjedev.bakjedev.UI;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Comparator;
import java.util.List;

public class Hud {
    private static MinecraftClient mc = MinecraftClient.getInstance();
    public static void Render(MatrixStack matrices, float tickDelta) {
        mc.textRenderer.drawWithShadow(matrices, "bakje.dev", 10, 10, -1);
        renderArrayList(matrices);
    }

    public static void renderArrayList(MatrixStack matrices) {
        if (mc.currentScreen==null) {
            int index = 0;
            int sWidth = mc.getWindow().getScaledWidth();
            int sHeight = mc.getWindow().getScaledHeight();

            List<Mod> enabled = ModuleManager.INSTANCE.getEnabledModules();

            enabled.sort(Comparator.comparingInt(m -> (int)mc.textRenderer.getWidth(((Mod)m).getDisplayName())).reversed());

            for(Mod mod : enabled) {
                if (mod.isVisible()) {
                    mc.textRenderer.drawWithShadow(matrices, mod.getDisplayName(), (sWidth - 4) - mc.textRenderer.getWidth(mod.getDisplayName()), 10 + (index * mc.textRenderer.fontHeight), -1);
                }
                index++;
            }
        }
    }
}

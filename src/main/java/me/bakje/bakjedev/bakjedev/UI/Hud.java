package me.bakje.bakjedev.bakjedev.UI;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.Render.HudModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.round;

public class Hud {
    private static MinecraftClient mc = MinecraftClient.getInstance();
    public static void Render(MatrixStack matrices, float tickDelta) {
        boolean nether = mc.world.getRegistryKey().getValue().getPath().contains("nether");
        BlockPos pos = mc.player.getBlockPos();
        BlockPos oppositePos = nether ? new BlockPos(mc.player.getPos().multiply(8, 1, 8))
                : new BlockPos(mc.player.getPos().multiply(0.125, 1, 0.125));
        double yaw = roundToPlace(MathHelper.wrapDegrees(mc.player.getYaw()), 1);
        double pitch = roundToPlace(MathHelper.wrapDegrees(mc.player.getPitch()), 1);
        Text xzdir;
        Text textDir;
        if (mc.player.getMovementDirection()== Direction.NORTH) {
            xzdir = Text.literal("-Z").formatted(Formatting.WHITE);
            textDir = Text.literal("north").formatted(Formatting.WHITE);
        } else if (mc.player.getMovementDirection()==Direction.EAST) {
            xzdir = Text.literal("+X").formatted(Formatting.WHITE);
            textDir = Text.literal("east").formatted(Formatting.WHITE);
        } else if (mc.player.getMovementDirection()==Direction.SOUTH) {
            xzdir = Text.literal("+Z").formatted(Formatting.WHITE);
            textDir = Text.literal("south").formatted(Formatting.WHITE);
        } else {
            xzdir = Text.literal("-X").formatted(Formatting.WHITE);
            textDir = Text.literal("west").formatted(Formatting.WHITE);
        }

        Text XYZ = Text.literal("XYZ").formatted(Formatting.GRAY);
        Text Dir = Text.literal("DIR").formatted(Formatting.GRAY);

        Text openBrackets = Text.literal("[").formatted(Formatting.GRAY);
        Text closingBrackets = Text.literal("]").formatted(Formatting.GRAY);
        Text commaSeparator = Text.literal(", ").formatted(Formatting.GRAY);

        Text xText = Text.literal(separateBigNumber(pos.getX())).formatted(Formatting.WHITE);
        Text yText = Text.literal(separateBigNumber(pos.getY())).formatted(Formatting.WHITE);
        Text zText = Text.literal(separateBigNumber(pos.getZ())).formatted(Formatting.WHITE);
        Text overworldCoordsText = xText.copy().append(commaSeparator).append(yText).append(commaSeparator).append(zText);

        Text netherXText = Text.literal(separateBigNumber(oppositePos.getX())).formatted(Formatting.WHITE);
        Text netherZText = Text.literal(separateBigNumber(oppositePos.getZ())).formatted(Formatting.WHITE);
        Text netherCoordsText = netherXText.copy().append(commaSeparator).append(netherZText);
        Text coordsText = overworldCoordsText.copy().append(" ").append(openBrackets).append(netherCoordsText).append(closingBrackets);

        Text YawPitchText = Text.literal(String.valueOf(yaw) + " " + String.valueOf(pitch)).formatted(Formatting.WHITE);
        Text DirText = (YawPitchText).copy().append(" ").append(openBrackets).append(xzdir).append(closingBrackets).append(" ").append(textDir);

        if (ModuleManager.INSTANCE.getModule(HudModule.class).isEnabled()) {
            mc.textRenderer.drawWithShadow(matrices, "bakje.dev", 10, 10, -1);
            mc.textRenderer.drawWithShadow(matrices, Dir.copy().append(" ").append(DirText), 1, (mc.getWindow().getHeight() / 2) - 1 - (mc.textRenderer.fontHeight * 2), -1);
            mc.textRenderer.drawWithShadow(matrices, XYZ.copy().append(" ").append(coordsText), 1, (mc.getWindow().getHeight() / 2) - 1 - mc.textRenderer.fontHeight, -1);
            renderArrayList(matrices);
        }
    }

    public static void renderArrayList(MatrixStack matrices) {
        if (mc.currentScreen==null) {
            int index = 0;//        }

            int sWidth = mc.getWindow().getScaledWidth();
            int sHeight = mc.getWindow().getScaledHeight();

            List<Mod> enabled = ModuleManager.INSTANCE.getEnabledModules();

            enabled.sort(Comparator.comparingInt(m -> (int)mc.textRenderer.getWidth(((Mod)m).getDisplayName())).reversed());

            for(Mod mod : enabled) {

                mc.textRenderer.drawWithShadow(matrices, mod.getDisplayName(), (sWidth - 4) - mc.textRenderer.getWidth(mod.getDisplayName()), 10 + (index * mc.textRenderer.fontHeight), -1);
                index++;
            }
        }
    }

    private static double roundToPlace(double value, int place) {
        if (place < 0) {
            return value;
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(place, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private static String separateBigNumber(int value) {
        DecimalFormat df = new DecimalFormat("#,###");
        String formattedValue = df.format(value);
        return formattedValue;
    }
}

package me.bakje.bakjedev.bakjedev.UI;

import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.Render.HudModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.round;

public class Hud {
    private static MinecraftClient mc = MinecraftClient.getInstance();
    public static void Render(MatrixStack matrices, float tickDelta) {
        if (ModuleManager.INSTANCE.getModule(HudModule.class).isEnabled()) {


            //WATERMARK
            Text watermarkText = Text.literal("bakje.dev ");
            Text watermarkVersion = Text.literal(Bakjedev.VERSION).formatted(Formatting.GRAY);

            //COORD HUD
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

            //TPS
            Text TPSName = Text.literal(" TPS").formatted(Formatting.GRAY);
            Formatting TPSColor;
            double TPS = ModuleManager.INSTANCE.getModule(HudModule.class).tps;
            if (TPS>15 && TPS<=20) {
                TPSColor = Formatting.WHITE;
            } else if (TPS > 10 && TPS<=15) {
                TPSColor = Formatting.YELLOW;
            }else if (TPS >5 && TPS<=10) {
                TPSColor = Formatting.GOLD;
            } else if (TPS<5) {
                TPSColor = Formatting.RED;
            } else {
                TPSColor = Formatting.LIGHT_PURPLE;
            }
            Text TPSText = Text.literal(String.valueOf(roundToPlace(TPS, 2))).formatted(TPSColor);

            //TIME SINCE LAST TICK
            long sinceTick = ModuleManager.INSTANCE.getModule(HudModule.class).lastPacket;
            long time = System.currentTimeMillis();
            Text TSLTText = Text.literal(String.valueOf(roundToPlace((time - sinceTick)/1000, 1)));
            Text TSLTName = Text.literal(" Sec").formatted(Formatting.GRAY);

            //FPS
            Text FPSText = Text.literal(String.valueOf(MinecraftClient.currentFps));
            Text FPSName = Text.literal(" FPS").formatted(Formatting.GRAY);


            if (ModuleManager.INSTANCE.getModule(HudModule.class).info.isEnabled()) {
                mc.textRenderer.drawWithShadow(matrices, watermarkText.copy().append(watermarkVersion), 1, (mc.getWindow().getHeight() / 4) + mc.textRenderer.fontHeight, -1);
                mc.textRenderer.drawWithShadow(matrices, TPSText.copy().append(TPSName), 1, (mc.getWindow().getHeight() / 4) + mc.textRenderer.fontHeight * 2 + 2, -1);
                mc.textRenderer.drawWithShadow(matrices, TSLTText.copy().append(TSLTName), 1, (mc.getWindow().getHeight() / 4) + mc.textRenderer.fontHeight * 3 + 4, -1);
                mc.textRenderer.drawWithShadow(matrices, FPSText.copy().append(FPSName), 1, (mc.getWindow().getHeight() / 4) + mc.textRenderer.fontHeight * 4 + 6, -1);
            }
            if (ModuleManager.INSTANCE.getModule(HudModule.class).dir.isEnabled())
                mc.textRenderer.drawWithShadow(matrices, Dir.copy().append(" ").append(DirText), 1, (mc.getWindow().getHeight() / 2) - 1 - (mc.textRenderer.fontHeight * 2), -1);
            if (ModuleManager.INSTANCE.getModule(HudModule.class).coords.isEnabled())
                mc.textRenderer.drawWithShadow(matrices, XYZ.copy().append(" ").append(coordsText), 1, (mc.getWindow().getHeight() / 2) - 1 - mc.textRenderer.fontHeight, -1);
            if (ModuleManager.INSTANCE.getModule(HudModule.class).arraylist.isEnabled())
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
                if (ModuleManager.INSTANCE.getModule(mod.getClass()).isVisible()) {
                    mc.textRenderer.drawWithShadow(matrices, mod.getDisplayName(), (sWidth - 4) - mc.textRenderer.getWidth(mod.getDisplayName()), 10 + (index * mc.textRenderer.fontHeight), -1);
                    index++;
                }
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

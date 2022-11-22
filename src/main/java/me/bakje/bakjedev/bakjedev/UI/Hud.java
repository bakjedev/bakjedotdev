package me.bakje.bakjedev.bakjedev.UI;

import com.mojang.blaze3d.systems.RenderSystem;
import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.UI.Screens.clickgui.ClickGui;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.Render.HudModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.SetBlockCommand;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.round;

public class Hud {
    private static MinecraftClient mc = MinecraftClient.getInstance();
    static float hue = 0;
    static int mainColor;
    static int accentColor;

    public static void Render(MatrixStack matrices, float tickDelta) {
        if (ModuleManager.INSTANCE.getModule(HudModule.class).isEnabled()) {
            int screenMiddle = (mc.getWindow().getHeight() / 4) - 30;

            String theme = ModuleManager.INSTANCE.getModule(HudModule.class).theme.getMode();
            if (theme=="Mahan") {
                accentColor = 0x00AAAA;
                mainColor = 0x55FFFF;
            } else {
                accentColor = 0xAAAAAA;
                mainColor = 0xFFFFFF;
            }

            //WATERMARK

            Text watermarkText;
            Text watermarkVersion = Text.literal(Bakjedev.VERSION).styled(style -> style.withColor(accentColor));
            if (theme=="bakje.dev" || theme=="Mahan") {
                Text watermarkName = Text.literal("bakje.dev ").styled(style -> style.withColor(mainColor));
                watermarkText = watermarkName.copy().append(watermarkVersion);
            } else if (theme=="BSB") {
                watermarkText = fancyRainbow("BSB Hack");
            } else if (theme=="bakje.dev2") {
                watermarkText = fancyRainbow("bakje.dev");
            } else if(theme=="Ruhama") {
                Text watermarkName = Text.literal("Ruhama Client ").styled(style -> style.withColor(mainColor));
                watermarkText = watermarkName.copy().append(watermarkVersion);
            }else {
                watermarkText = Text.literal("hi");
            }

            // COORD HUD
            boolean nether = mc.world.getRegistryKey().getValue().getPath().contains("nether");
            BlockPos pos = mc.player.getBlockPos();
            BlockPos oppositePos = nether ? new BlockPos(mc.player.getPos().multiply(8, 1, 8))
                    : new BlockPos(mc.player.getPos().multiply(0.125, 1, 0.125));
            double yaw = roundToPlace(MathHelper.wrapDegrees(mc.player.getYaw()), 1);
            double pitch = roundToPlace(MathHelper.wrapDegrees(mc.player.getPitch()), 1);
            Text xzdir;
            Text textDir;
            if (mc.player.getMovementDirection() == Direction.NORTH) {
                xzdir = Text.literal("-Z").formatted(Formatting.WHITE);
                textDir = Text.literal("north").formatted(Formatting.WHITE);
            } else if (mc.player.getMovementDirection() == Direction.EAST) {
                xzdir = Text.literal("+X").formatted(Formatting.WHITE);
                textDir = Text.literal("east").formatted(Formatting.WHITE);
            } else if (mc.player.getMovementDirection() == Direction.SOUTH) {
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


            // FPS
            Text FPSText = Text.literal(String.valueOf(MinecraftClient.currentFps)).styled(style -> style.withColor(mainColor));
            Text FPSName = Text.literal(" FPS").styled(style -> style.withColor(accentColor));


            // TPS
            Text TPSName = Text.literal(" TPS").styled(style -> style.withColor(accentColor));
            int TPSColor;
            double TPS = ModuleManager.INSTANCE.getModule(HudModule.class).tps;
            if (TPS > 15 && TPS <= 20) {
                TPSColor = theme=="Mahan" ? 0x55FFFF : 0xFFFFFF;
            } else if (TPS > 10 && TPS <= 15) {
                TPSColor = 0xFFFF55;
            } else if (TPS > 5 && TPS <= 10) {
                TPSColor = 0xFFAA00;
            } else if (TPS < 5) {
                TPSColor = 0xFF5555;
            } else {
                TPSColor = 0xFF55FF;
            }
            Text TPSText = Text.literal(String.valueOf(roundToPlace(TPS, 2))).styled(style -> style.withColor(TPSColor));

            // TIME SINCE LAST TICK
            double sinceTick = ModuleManager.INSTANCE.getModule(HudModule.class).lastPacket;
            double time = System.currentTimeMillis();
            Text TSLTText;
            if (((time - sinceTick) / 1000) < 0.2) {
                TSLTText = Text.literal("0.0").styled(style -> style.withColor(mainColor));
            } else {
                TSLTText = Text.literal(Double.toString(roundToPlace((time - sinceTick) / 1000, 1))).styled(style -> style.withColor(mainColor));
            }
            Text TSLTName = Text.literal(" Sec").styled(style -> style.withColor(accentColor));

            //PING
            PlayerListEntry player = mc.player.networkHandler.getPlayerListEntry(mc.player.getGameProfile().getId());
            int latency = player == null ? 0 : player.getLatency();
            Text latencyText = Text.literal(Integer.toString(latency)).styled(style -> style.withColor(mainColor));
            Text latencyName = Text.literal(" MS").styled(style -> style.withColor(accentColor));

            // PLAYERS
            int playerCount = mc.getNetworkHandler().getPlayerList().size();
            Text playerText = Text.literal(Integer.toString(playerCount)).styled(style -> style.withColor(mainColor));
            Text playerName = Text.literal(playerCount > 1 ? " Players" : " Player").styled(style -> style.withColor(accentColor));

            // SPEED
            Vec3d vec = new Vec3d(mc.player.getX() - mc.player.prevX, 0, mc.player.getZ() - mc.player.prevZ).multiply(20);
            final double speed = roundToPlace((Math.abs(vec.length())) * 3.6, 2);
            Text speedText = Text.literal(Double.toString(speed)).styled(style -> style.withColor(mainColor));
            Text speedName = Text.literal(" Km/h").styled(style -> style.withColor(accentColor));


            if (ModuleManager.INSTANCE.getModule(HudModule.class).info.isEnabled()) {
                mc.textRenderer.drawWithShadow(matrices, watermarkText, 1, screenMiddle + mc.textRenderer.fontHeight, -1);
                mc.textRenderer.drawWithShadow(matrices, FPSText.copy().append(FPSName), 1, screenMiddle + mc.textRenderer.fontHeight * 2 + 2, -1);
                mc.textRenderer.drawWithShadow(matrices, TPSText.copy().append(TPSName), 1, screenMiddle + mc.textRenderer.fontHeight * 3 + 4, -1);
                mc.textRenderer.drawWithShadow(matrices, TSLTText.copy().append(TSLTName), 1, screenMiddle + mc.textRenderer.fontHeight * 4 + 6, -1);
                mc.textRenderer.drawWithShadow(matrices, latencyText.copy().append(latencyName), 1, screenMiddle + mc.textRenderer.fontHeight * 5 + 8, -1);
                mc.textRenderer.drawWithShadow(matrices, playerText.copy().append(playerName), 1, screenMiddle + mc.textRenderer.fontHeight * 6 + 10, -1);
                mc.textRenderer.drawWithShadow(matrices, speedText.copy().append(speedName), 1, screenMiddle + mc.textRenderer.fontHeight * 7 + 12, -1);
            }
            if (ModuleManager.INSTANCE.getModule(HudModule.class).dir.isEnabled())
                mc.textRenderer.drawWithShadow(matrices, Dir.copy().append(" ").append(DirText), 1, (mc.getWindow().getHeight() / 2) - 1 - (mc.textRenderer.fontHeight * 2), -1);
            if (ModuleManager.INSTANCE.getModule(HudModule.class).coords.isEnabled())
                mc.textRenderer.drawWithShadow(matrices, XYZ.copy().append(" ").append(coordsText), 1, (mc.getWindow().getHeight() / 2) - 1 - mc.textRenderer.fontHeight, -1);
            if (ModuleManager.INSTANCE.getModule(HudModule.class).arraylist.isEnabled())
                renderArrayList(matrices);
            if (ModuleManager.INSTANCE.getModule(HudModule.class).armor.isEnabled()) {
                drawArmor(matrices, 493, 485);
            }
        }
    }

    public static void renderArrayList(MatrixStack matrices) {
        if (!(mc.currentScreen instanceof ClickGui)) {
            int index = 0;//        }

            int sWidth = mc.getWindow().getScaledWidth();
            int sHeight = mc.getWindow().getScaledHeight();

            List<Mod> enabled = ModuleManager.INSTANCE.getEnabledModules();

            enabled.sort(Comparator.comparingInt(m -> (int) mc.textRenderer.getWidth(((Mod) m).getDisplayName())).reversed());

            for (Mod mod : enabled) {
                if (ModuleManager.INSTANCE.getModule(mod.getClass()).isVisible()) {
                    if (ModuleManager.INSTANCE.getModule(HudModule.class).arraylistRainbow.isMode("H")) {
                        mc.textRenderer.drawWithShadow(matrices, fancyRainbow(mod.getDisplayName()), (sWidth - 4) - mc.textRenderer.getWidth(mod.getDisplayName()), 10 + (index * mc.textRenderer.fontHeight), -1);
                        index++;
                    } else if (ModuleManager.INSTANCE.getModule(HudModule.class).arraylistRainbow.isMode("V")) {
                        mc.textRenderer.drawWithShadow(matrices, mod.getDisplayName(), (sWidth - 4) - mc.textRenderer.getWidth(mod.getDisplayName()), 10 + (index * mc.textRenderer.fontHeight), getRainbow(1,1,20, index*150));
                        index++;
                    }
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

    private static void drawArmor(MatrixStack matrices, int x, int y) {
        for (int count = 0; count < mc.player.getInventory().armor.size(); count++) {
            ItemStack is = mc.player.getInventory().armor.get(3-count);
            if (is.isEmpty())
                continue;

            int curX = x + count * 19;
            int curY = y;
            RenderSystem.enableDepthTest();
            mc.getItemRenderer().renderGuiItemIcon(is, curX, curY);

            int durcolor = is.isDamageable() ? 0xff000000 | MathHelper.hsvToRgb((float) (is.getMaxDamage() - is.getDamage()) / is.getMaxDamage() / 3.0F, 1.0F, 1.0F) : 0;

            matrices.push();
            matrices.translate(0, 0, mc.getItemRenderer().zOffset + 200);

            if (is.getCount() > 1) {
                matrices.push();
                String s = Integer.toString(is.getCount());

                matrices.translate(curX + 19 - mc.textRenderer.getWidth(s), curY + 9, 0);
                matrices.scale(0.85f, 0.85f, 1f);

                mc.textRenderer.drawWithShadow(matrices, s, 0, 0, 0xffffff);
                matrices.pop();
            }
            if (is.isDamageable() && is.getMaxDamage()-is.getDamage()!=is.getMaxDamage()) {
                int barLength = Math.round(13.0F - is.getDamage() * 13.0F / is.getMaxDamage());
                DrawableHelper.fill(matrices, curX + 2, curY + 13, curX + 15, curY + 15, 0xff000000);
                DrawableHelper.fill(matrices, curX + 2, curY + 13, curX + 2 + barLength, curY + 14, durcolor);
            }
            matrices.pop();
        }
    }
    public static int getRainbow(float sat, float bri, double speed, int offset) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + offset) / speed) % 360;
        return 0xff000000 | MathHelper.hsvToRgb((float) (rainbowState / 360.0), sat, bri);
    }

    public static MutableText fancyRainbow(String text) {
        String drawString = text;
        MutableText drawText = Text.literal("");
        int hue = MathHelper.floor((System.currentTimeMillis() % 5000L) / 5000.0F * 360.0F);

        for (char c : drawString.toCharArray()) {
            int finalHue = hue;
            drawText.append(Text.literal(Character.toString(c)).styled(s -> s.withColor(MathHelper.hsvToRgb(finalHue / 360.0F, 1.0F, 1.0F))));
            hue += 100 / drawString.length();
            if (hue >= 360) hue %= 360;
        }
        return drawText;
    }
}
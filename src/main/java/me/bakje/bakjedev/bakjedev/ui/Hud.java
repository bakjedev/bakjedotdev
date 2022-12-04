package me.bakje.bakjedev.bakjedev.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.ui.screens.clickgui.ClickGui;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.movement.Timer;
import me.bakje.bakjedev.bakjedev.module.render.HudModule;
import me.bakje.bakjedev.bakjedev.util.TimeUtil;
import me.bakje.bakjedev.bakjedev.util.bakjeRandomUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static me.bakje.bakjedev.bakjedev.util.bakjeRandomUtil.*;

public class Hud {
    private static MinecraftClient mc = MinecraftClient.getInstance();
    static float hue = 0;
    static int mainColor;
    static int accentColor;
    static int counter;
    static int notifX = 0;
    static Text notifText = Text.literal("");
    static boolean notif = false;
    static TimeUtil timer = new TimeUtil();

    public static void Render(MatrixStack matrices, float tickDelta) {
        if (ModuleManager.INSTANCE.getModule(HudModule.class).isEnabled()) {
            int screenMiddle = (mc.getWindow().getHeight() / 4) - 30;

            int theme = ModuleManager.INSTANCE.getModule(HudModule.class).theme.getIndex();
            if (theme==3) {
                accentColor = 0x00AAAA;
                mainColor = 0x55FFFF;
            } else if (theme==5){
                accentColor = Color.WHITE.getRGB();
                mainColor = new Color(85, 85, 255).getRGB();
            } else {
                accentColor = 0xAAAAAA;
                mainColor = 0xFFFFFF;
            }

            //WATERMARK

            Text watermarkText;
            Text watermarkVersion = Text.literal(Bakjedev.VERSION).styled(style -> style.withColor(accentColor));
            if (theme==0 || theme==3) {
                Text watermarkName = Text.literal("bakje.dev ").styled(style -> style.withColor(mainColor));
                watermarkText = watermarkName.copy().append(watermarkVersion);
            } else if (theme==2) {
                watermarkText = fancyRainbow("BSB Hack");
            } else if (theme==1) {
                watermarkText = fancyRainbow("bakje.dev");
            } else if(theme==4 ) {
                Text watermarkName = Text.literal("Ruhama Client ").styled(style -> style.withColor(mainColor));
                watermarkText = watermarkName.copy().append(watermarkVersion);
            }else if (theme==5){
                watermarkText = Text.literal("BleachHack epearl edition ").styled(style -> style.withColor(mainColor)).append(Text.literal("b2500+").styled(style -> style.withColor(Formatting.WHITE)));
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

            Text XYZ;
            if (theme==5) {
                XYZ = Text.literal("XYZ").styled(style -> style.withColor(mainColor));
            } else {
                XYZ = Text.literal("XYZ").formatted(Formatting.GRAY);
            }
            Text Dir = Text.literal("DIR").formatted(Formatting.GRAY);

            Text openBrackets = Text.literal("[").formatted(Formatting.GRAY);
            Text closingBrackets = Text.literal("]").formatted(Formatting.GRAY);
            Text commaSeparator = Text.literal(", ").formatted(Formatting.GRAY);

            Text xText = Text.literal(separateBigNumber(pos.getX())).formatted(Formatting.WHITE);
            Text yText = Text.literal(separateBigNumber(pos.getY())).formatted(Formatting.WHITE);
            Text zText = Text.literal(separateBigNumber(pos.getZ())).formatted(Formatting.WHITE);
            Text overworldCoordsText;
            if (theme==5) {
                overworldCoordsText = xText.copy().append(" ").append(yText).append(" ").append(zText);
            } else {
                overworldCoordsText = xText.copy().append(commaSeparator).append(yText).append(commaSeparator).append(zText);
            }

            Text netherXText = Text.literal(separateBigNumber(oppositePos.getX())).formatted(Formatting.WHITE);
            Text netherZText = Text.literal(separateBigNumber(oppositePos.getZ())).formatted(Formatting.WHITE);
            Text netherCoordsText;
            if (theme==5) {
                netherCoordsText = netherXText.copy().append(" ").append(yText).append("").append(netherZText);
            } else {
                netherCoordsText = netherXText.copy().append(commaSeparator).append(netherZText);
            }
            Text coordsText;
            if (theme==5) {
                coordsText = overworldCoordsText.copy().append(" ").append(openBrackets).append(netherCoordsText).append(closingBrackets);
            } else {
                coordsText = overworldCoordsText.copy().append(" ").append(openBrackets).append(netherCoordsText).append(closingBrackets);
            }
            Text YawPitchText = Text.literal(String.valueOf(yaw) + " " + String.valueOf(pitch)).formatted(Formatting.WHITE);
            Text DirText = (YawPitchText).copy().append(" ").append(openBrackets).append(xzdir).append(closingBrackets).append(" ").append(textDir);


            // FPS
            Text FPSText;
            Text FPSName;
            if (theme==5) {
                FPSText = Text.literal(String.valueOf(MinecraftClient.currentFps)).styled(style -> style.withColor(accentColor));
                FPSName = Text.literal("FPS").styled(style -> style.withColor(mainColor));
            } else {
                FPSText = Text.literal(String.valueOf(MinecraftClient.currentFps)).styled(style -> style.withColor(mainColor));
                FPSName = Text.literal("FPS").styled(style -> style.withColor(accentColor));
            }

            // TPS
            Text TPSName = Text.literal("TPS").styled(style -> style.withColor(theme==5 ? mainColor : accentColor));
            int TPSColor;
            double TPS = ModuleManager.INSTANCE.getModule(HudModule.class).tps;
            if (TPS > 15 && TPS <= 20) {
                TPSColor = theme==3 ? 0x55FFFF : 0xFFFFFF;
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

            // SERVER
            String server = mc.getCurrentServerEntry() == null ? "Singleplayer" : mc.getCurrentServerEntry().address;
            Text serverName = Text.literal("IP").styled(style -> style.withColor(mainColor));
            Text serverText = Text.literal(server).styled(style -> style.withColor(accentColor));

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
            Text latencyText;
            Text latencyName;
            if (theme==5) {
                latencyName = Text.literal("Ping").styled(style -> style.withColor(mainColor));
                latencyText = Text.literal(Integer.toString(latency)).styled(style -> style.withColor(accentColor));
            } else {
                latencyText = Text.literal(Integer.toString(latency)).styled(style -> style.withColor(mainColor));
                latencyName = Text.literal(" MS").styled(style -> style.withColor(accentColor));
            }
            // PLAYERS
            int playerCount = mc.getNetworkHandler().getPlayerList().size();
            Text playerText = Text.literal(Integer.toString(playerCount)).styled(style -> style.withColor(mainColor));
            Text playerName = Text.literal(playerCount > 1 ? " Players" : " Player").styled(style -> style.withColor(accentColor));

            // SPEED
            Vec3d vec = new Vec3d(mc.player.getX() - mc.player.prevX, 0, mc.player.getZ() - mc.player.prevZ).multiply(20);
            final double speed;
            if (ModuleManager.INSTANCE.getModule(Timer.class).isEnabled()) {
                speed = roundToPlace(((Math.abs(vec.length())) * (theme!=5 ? 3.6 : 1)) * ModuleManager.INSTANCE.getModule(Timer.class).speed.getValue(), 2);
            } else {
                speed = roundToPlace((Math.abs(vec.length())) * (theme!=5 ? 3.6 : 1), 2);
            }
            Text speedText;
            Text speedName;
            if (theme==5) {
                speedText = Text.literal(Double.toString(speed)).styled(style -> style.withColor(accentColor));
                speedName = Text.literal("BPS").styled(style -> style.withColor(mainColor));
            } else {
                speedText = Text.literal(Double.toString(speed)).styled(style -> style.withColor(mainColor));
                speedName = Text.literal(" Km/h").styled(style -> style.withColor(accentColor));
            }

            // NOTIFICATION
            if (notif) {
                if (timer.passed(10)) {
                    counter++;
                    timer.reset();
                }
                if (counter>160 && notifX< -75) {
                    notifText= Text.literal("");
                    notifX = 0;
                    notif=false;
                }
                if (notifX<2 && counter<161) {
                    notifX++;
                }
                if (counter> 160 && notifX>(-notifText.getString().length())*6) {
                    notifX=notifX-mc.textRenderer.getWidth("A");
                }
            }

            // WELCOME
            Text welcomeWord = Text.literal("Welcome ").styled(style -> style.withColor(theme==5 ? mainColor : accentColor));
            String welcomeName = mc.session.getUsername().toString();
            Text welcomeText = welcomeWord.copy().append(welcomeName);

            int welcomeX = (mc.getWindow().getWidth()/4) - ((mc.textRenderer.getWidth(welcomeText) / 2));


            // TIME
            String timeNow = new SimpleDateFormat("h:mm" + " a").format(new Date());
            Text timeName = Text.literal("Time").styled(style -> style.withColor(mainColor));
            Text timeText = Text.literal(timeNow).styled(style -> style.withColor(accentColor));

            // EFFECTS
            double height = 0;

            for (StatusEffectInstance statusEffectInstance : mc.player.getStatusEffects()) {
                height += mc.textRenderer.fontHeight;
            }


            if (mc.crosshairTarget instanceof EntityHitResult) {
                Entity e = ((EntityHitResult) mc.crosshairTarget).getEntity();
                if (e instanceof DonkeyEntity) {
                    boolean breedingTime = ((DonkeyEntity) e).isReadyToBreed();
                    Text breedingText = Text.literal(String.valueOf(breedingTime)).styled(style -> style.withColor(mainColor));
                    mc.textRenderer.drawWithShadow(matrices, breedingText, mc.getWindow().getWidth()-mc.textRenderer.getWidth(breedingText)-2, 100, 0xFFFFFF);
                }
            }


            HudModule hudModule = ModuleManager.INSTANCE.getModule(HudModule.class);
            if (hudModule.info.isEnabled()) {
                if (hudModule.theme.getIndex()==5) {
                    mc.textRenderer.drawWithShadow(matrices, watermarkText, 1, 1, -1);
                    mc.textRenderer.drawWithShadow(matrices, welcomeText, 1, (mc.getWindow().getHeight()/4) + 1, 0xFFFFFF);
                    mc.textRenderer.drawWithShadow(matrices, XYZ.copy().append(" ").append(coordsText), 1, (mc.getWindow().getHeight() / 4) +2 + (mc.textRenderer.fontHeight), -1);
                    mc.textRenderer.drawWithShadow(matrices, TPSName.copy().append(" ").append(TPSText), 1, (mc.getWindow().getHeight() / 4) +3 + (mc.textRenderer.fontHeight * 2) , -1);
                    mc.textRenderer.drawWithShadow(matrices, serverName.copy().append(" ").append(serverText), 1, (mc.getWindow().getHeight() / 4) +4 + (mc.textRenderer.fontHeight * 3) , -1);
                    mc.textRenderer.drawWithShadow(matrices, timeName.copy().append(" ").append(timeText), 1, (mc.getWindow().getHeight() / 4) +5 + (mc.textRenderer.fontHeight * 4) , -1);
                    mc.textRenderer.drawWithShadow(matrices, latencyName.copy().append(" ").append(latencyText), 1, (mc.getWindow().getHeight() / 4) +6 + (mc.textRenderer.fontHeight * 5) , -1);
                    mc.textRenderer.drawWithShadow(matrices, FPSName.copy().append(" ").append(FPSText), 1, (mc.getWindow().getHeight() / 4) +7 + (mc.textRenderer.fontHeight * 6) , -1);
                    mc.textRenderer.drawWithShadow(matrices, speedName.copy().append(" ").append(speedText), 1, (mc.getWindow().getHeight() / 4) +8 + (mc.textRenderer.fontHeight * 7) , -1);

                } else {
                    mc.textRenderer.drawWithShadow(matrices, watermarkText, 1, screenMiddle + mc.textRenderer.fontHeight, -1);
                    mc.textRenderer.drawWithShadow(matrices, FPSText.copy().append(" ").append(FPSName), 1, screenMiddle + mc.textRenderer.fontHeight * 2 + 2, -1);
                    mc.textRenderer.drawWithShadow(matrices, TPSText.copy().append(" ").append(TPSName), 1, screenMiddle + mc.textRenderer.fontHeight * 3 + 4, -1);
                    mc.textRenderer.drawWithShadow(matrices, TSLTText.copy().append(TSLTName), 1, screenMiddle + mc.textRenderer.fontHeight * 4 + 6, -1);
                    mc.textRenderer.drawWithShadow(matrices, latencyText.copy().append(latencyName), 1, screenMiddle + mc.textRenderer.fontHeight * 5 + 8, -1);
                    mc.textRenderer.drawWithShadow(matrices, playerText.copy().append(playerName), 1, screenMiddle + mc.textRenderer.fontHeight * 6 + 10, -1);
                    mc.textRenderer.drawWithShadow(matrices, speedText.copy().append(speedName), 1, screenMiddle + mc.textRenderer.fontHeight * 7 + 12, -1);
                }
                }
            if (hudModule.dir.isEnabled() && theme!=5)
                mc.textRenderer.drawWithShadow(matrices, Dir.copy().append(" ").append(DirText), 1, (mc.getWindow().getHeight() / 2) - 1 - (mc.textRenderer.fontHeight * 2), -1);

            if (hudModule.coords.isEnabled() && theme!=5)
                mc.textRenderer.drawWithShadow(matrices, XYZ.copy().append(" ").append(coordsText), 1, (mc.getWindow().getHeight() / 2) - 1 - mc.textRenderer.fontHeight, -1);

            if (hudModule.arraylist.isEnabled())
                renderArrayList(matrices);

            if (hudModule.armor.isEnabled())
                drawArmor(matrices, 493, 485);

            if (hudModule.notifications.isEnabled())
                mc.textRenderer.drawWithShadow(matrices, notifText, notifX, 10, 0xFFFFFF);

            if (hudModule.welcome.isEnabled() && theme!=5)
                mc.textRenderer.drawWithShadow(matrices, welcomeText, welcomeX, 3, 0xFFFFFF);

            if (hudModule.effects.isEnabled() && theme!=5) {
                int x = 2;
                int y = 2;

                for (StatusEffectInstance statusEffectInstance : mc.player.getStatusEffects()) {
                    StatusEffect statusEffect = statusEffectInstance.getEffectType();

                    int c = statusEffect.getColor();
                    int red = new Color(c).getRed();
                    int green = new Color(c).getGreen();
                    int blue = new Color(c).getBlue();

                    String text = getEffectString(statusEffectInstance);
                    mc.textRenderer.drawWithShadow(matrices, text, x, y, c);

                    red = green = blue = 255;
                    y += mc.textRenderer.fontHeight;
                }
            }
        }
    }

    public static void renderArrayList(MatrixStack matrices) {
        if (!(mc.currentScreen instanceof ClickGui)) {
            int index = 0;

            int sWidth = mc.getWindow().getScaledWidth();
            int sHeight = mc.getWindow().getScaledHeight();

            List<Mod> enabled = ModuleManager.INSTANCE.getEnabledModules();

            enabled.sort(Comparator.comparingInt(m -> (int) mc.textRenderer.getWidth(((Mod) m).getDisplayName())).reversed());

            for (Mod mod : enabled) {
                if (ModuleManager.INSTANCE.getModule(mod.getClass()).isVisible()) {
                    if (ModuleManager.INSTANCE.getModule(HudModule.class).arraylistRainbow.isMode("H") && ModuleManager.INSTANCE.getModule(HudModule.class).theme.getIndex()!=5) {
                        mc.textRenderer.drawWithShadow(matrices, fancyRainbow(mod.getDisplayName()), (sWidth - 2) - mc.textRenderer.getWidth(mod.getDisplayName()), 2 + (index * mc.textRenderer.fontHeight), -1);
                        index++;
                    } else if (ModuleManager.INSTANCE.getModule(HudModule.class).arraylistRainbow.isMode("V") && ModuleManager.INSTANCE.getModule(HudModule.class).theme.getIndex()!=5) {
                        mc.textRenderer.drawWithShadow(matrices, mod.getDisplayName(), (sWidth - 2) - mc.textRenderer.getWidth(mod.getDisplayName()), 2 + (index * mc.textRenderer.fontHeight), getRainbow(1,1,20, index*150));
                        index++;
                    } else if (ModuleManager.INSTANCE.getModule(HudModule.class).theme.getIndex()==5) {
                        mc.textRenderer.drawWithShadow(matrices, mod.getDisplayName(), 1, mc.textRenderer.fontHeight + 1 + (index * mc.textRenderer.fontHeight + index), mainColor);
                        index++;                    }
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



    public static void notification(Text message) {
        if (ModuleManager.INSTANCE.getModule(HudModule.class).notifications.isEnabled() && !notif) {
            notifText = message;
            counter = 0;
            notif = true;
            notifX= -message.getString().length();
        }
    }

    public static String getEffectString(StatusEffectInstance statusEffectInstance) {
        return String.format("%s %s (%s)", bakjeRandomUtil.getEffectNames(statusEffectInstance.getEffectType()), IntegerToRomanNumeral(statusEffectInstance.getAmplifier() + 1), StatusEffectUtil.durationToString(statusEffectInstance, 1));
    }


}
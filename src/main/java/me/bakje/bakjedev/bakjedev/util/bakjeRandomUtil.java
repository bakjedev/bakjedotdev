package me.bakje.bakjedev.bakjedev.util;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Map;

public class bakjeRandomUtil {

    //This is where i just throw util stuff that I don't care enough about to give it its own class.

    public static String IntegerToRomanNumeral(int input) {
        if (input < 1 || input > 3999)
            return "Invalid Roman Number Value";
        String s = "";
        while (input >= 1000) {
            s += "M";
            input -= 1000;        }
        while (input >= 900) {
            s += "CM";
            input -= 900;
        }
        while (input >= 500) {
            s += "D";
            input -= 500;
        }
        while (input >= 400) {
            s += "CD";
            input -= 400;
        }
        while (input >= 100) {
            s += "C";
            input -= 100;
        }
        while (input >= 90) {
            s += "XC";
            input -= 90;
        }
        while (input >= 50) {
            s += "L";
            input -= 50;
        }
        while (input >= 40) {
            s += "XL";
            input -= 40;
        }
        while (input >= 10) {
            s += "X";
            input -= 10;
        }
        while (input >= 9) {
            s += "IX";
            input -= 9;
        }
        while (input >= 5) {
            s += "V";
            input -= 5;
        }
        while (input >= 4) {
            s += "IV";
            input -= 4;
        }
        while (input >= 1) {
            s += "I";
            input -= 1;
        }
        return s;
    }

    private static final Map<StatusEffect, String> statusEffectNames = new HashMap<>(16);

    public static String getEffectNames(StatusEffect effect) {
        return statusEffectNames.computeIfAbsent(effect, effect1 -> StringHelper.stripTextFormat(effect1.getName().getString()));
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

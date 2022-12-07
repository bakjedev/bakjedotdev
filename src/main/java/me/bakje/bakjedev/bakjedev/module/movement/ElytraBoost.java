package me.bakje.bakjedev.bakjedev.module.movement;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.settings.NumberSetting;
import net.minecraft.util.math.MathHelper;

public class ElytraBoost extends Mod {
    public NumberSetting boostSpeed = new NumberSetting("BoostSpeed", 0, 0.15, 0.05, 0.01);
    public NumberSetting maxBoost = new NumberSetting("MaxBoost", 0, 5, 2.5, 0.1);
    public ElytraBoost() {
        super("ElytraBoost", "hacker boosts with wing", Category.MOVEMENT, true);
        addSettings(boostSpeed, maxBoost);
    }

    @Override
    public void onTick() {
        double currentVel = Math.abs(mc.player.getVelocity().x) + Math.abs(mc.player.getVelocity().y) + Math.abs(mc.player.getVelocity().z);
        float radianYaw = (float) Math.toRadians(mc.player.getYaw());
        float boost = this.boostSpeed.getValueFloat();
        if (mc.player.isFallFlying() && currentVel <= maxBoost.getValue()) {
            if (mc.options.backKey.isPressed()) {
                mc.player.addVelocity(MathHelper.sin(radianYaw) * boost, 0, MathHelper.cos(radianYaw) * -boost);
            } else if (mc.player.getPitch() > 0) {
                mc.player.addVelocity(MathHelper.sin(radianYaw) * -boost, 0, MathHelper.cos(radianYaw) * boost);
            }
        }
    }


}
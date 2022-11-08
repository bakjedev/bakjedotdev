package me.bakje.bakjedev.bakjedev.module.Combat;

import me.bakje.bakjedev.bakjedev.module.Settings.NumberSetting;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.BooleanSetting;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;


public class Aura extends Mod {
    public NumberSetting range = new NumberSetting("Range", 1, 10, 5.5, 0.1);
    public BooleanSetting rotate = new BooleanSetting("Rotate", false);
    public BooleanSetting attackEverything = new BooleanSetting("atackAll", false);
    boolean rotateCondition;
    boolean attackCondition;
    public Aura() {
        super("Aura", "kill", Category.COMBAT, true);
        addSettings(rotate, range, attackEverything);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onTick() {
        if (!mc.player.isAlive()) {
            return;
        }
            ClientPlayerEntity player = mc.player;
        Vec3d playerPos = player.getPos();
        double closestDistance = Double.POSITIVE_INFINITY;
        Entity closestEntity = null;
        for (Entity entity : mc.world.getEntities()) {
            Vec3d entityPos = entity.getPos();
            if (entity instanceof ItemEntity
                    || entity instanceof ExperienceOrbEntity
                    || entity instanceof ExperienceBottleEntity
                    || entity instanceof PotionEntity
                    || entity instanceof SnowballEntity
                    || entity instanceof EggEntity
                    || entity instanceof ArrowEntity
                    || entity instanceof FireworkRocketEntity) {
                return;
            }
            if (attackEverything.isEnabled()) {
                rotateCondition = entityPos.distanceTo(mc.player.getPos())<range.getValue() && entity!=mc.player;
                attackCondition = entityPos.distanceTo(mc.player.getPos())<closestDistance && entityPos.distanceTo(playerPos)<range.getValue() && entity!=mc.player;
            } else {
                rotateCondition = entityPos.distanceTo(mc.player.getPos())<range.getValue() && entity instanceof PlayerEntity && entity!=mc.player;
                attackCondition = entity instanceof PlayerEntity && entityPos.distanceTo(mc.player.getPos())<closestDistance && entityPos.distanceTo(playerPos)<range.getValue() && entity!=mc.player;
            }
            if (rotateCondition) {
                if (rotate.isEnabled()) {
                    double dX = mc.player.getX() - entity.getX();
                    double dY = mc.player.getY() - entity.getY();
                    double dZ = mc.player.getZ() - entity.getZ();

                    double DistanceXZ = Math.sqrt(dX * dX + dZ * dZ);
                    double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + dY * dY);

                    double newYaw = Math.acos(dX / DistanceXZ) * 180 / Math.PI;
                    double newPitch = Math.acos(dY / -DistanceY) * 180 / Math.PI - 90;

                    if (dZ < 0.0)
                        newYaw = newYaw + Math.abs(180 - newYaw) * 2;
                    newYaw = (newYaw + 90);
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), (float) newYaw, (float) newPitch, mc.player.isOnGround()));
                }
            }
            if (attackCondition) {
                closestDistance=entityPos.distanceTo(playerPos);
                closestEntity=entity;
                float cooldown = mc.player.getAttackCooldownProgress(mc.getTickDelta());
                if (cooldown==1) {
                    mc.interactionManager.attackEntity(mc.player, closestEntity);
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
            }
        }

        super.onTick();
    }
}

package me.bakje.bakjedev.bakjedev.module.Combat;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.BooleanSetting;
import me.bakje.bakjedev.bakjedev.module.Settings.NumberSetting;
import me.bakje.bakjedev.bakjedev.util.TimeUtil;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;


public class Aura extends Mod {
    public NumberSetting range = new NumberSetting("Range", 1, 10, 5.5, 0.1);
    public BooleanSetting rotate = new BooleanSetting("Rotate", false);
    public BooleanSetting attackEverything = new BooleanSetting("atackAll", false);
    boolean rotateCondition;
    boolean attackCondition;
    public Aura() {
        super("Aura", "kill", Category.COMBAT);
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

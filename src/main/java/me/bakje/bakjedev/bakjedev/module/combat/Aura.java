package me.bakje.bakjedev.bakjedev.module.combat;

import com.google.common.collect.Streams;
import me.bakje.bakjedev.bakjedev.module.settings.NumberSetting;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.settings.BooleanSetting;
import me.bakje.bakjedev.bakjedev.util.world.EntityUtil;
import me.bakje.bakjedev.bakjedev.util.world.WorldUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Aura extends Mod {
    private int delay = 0;
    public NumberSetting range = new NumberSetting("Range", 1, 10, 5.5, 0.1);
    public NumberSetting cps = new NumberSetting("CPS", 0, 20, 8, 1);
    public BooleanSetting rotate = new BooleanSetting("Rotate", false);
    public BooleanSetting raycast = new BooleanSetting("Raycast", false);
    public BooleanSetting onePointNineDelay = new BooleanSetting("1.9 delay", true);
    public BooleanSetting players = new BooleanSetting("Players", true);
    public BooleanSetting mobs = new BooleanSetting("Mobs", false);
    public BooleanSetting animals = new BooleanSetting("Animals", false);
    public BooleanSetting armorStands = new BooleanSetting("ArmorStands", false);
    public BooleanSetting projectiles = new BooleanSetting("Projectiles", true);
    public Aura() {
        super("Aura", "kill", Category.COMBAT, true);
        addSettings(players, mobs, animals, armorStands, projectiles, onePointNineDelay, cps, range, raycast, rotate);
    }

    @Override
    public void onTick() {
        if (!mc.player.isAlive()) {
            return;
        }
        delay++;
        int reqDelay = (int) Math.rint(20/this.cps.getValue());
        boolean cooldownDone = this.onePointNineDelay.isEnabled()
                ? mc.player.getAttackCooldownProgress(mc.getTickDelta()) == 1.0f
                : (delay > reqDelay || reqDelay==0);

        if (cooldownDone) {
            for (Entity e: getEntities()) {
                boolean shouldRotate = this.rotate.isEnabled() && DebugRenderer.getTargetedEntity(mc.player, 7).orElse(null) != e;

                if (shouldRotate) {
                    WorldUtil.facePosAuto(e.getX(), e.getY() + e.getHeight() / 2, e.getZ());
                }

                boolean wasSprinting = mc.player.isSprinting();

                if (wasSprinting)
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));

                mc.interactionManager.attackEntity(mc.player, e);
                mc.player.swingHand(Hand.MAIN_HAND);

                if (wasSprinting)
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));

                delay = 0;
            }
        }
    }

    private List<Entity> getEntities() {
        Stream<Entity> targets;


        targets = Streams.stream(mc.world.getEntities());


        Comparator<Entity> comparator;


        comparator = Comparator.comparing(mc.player::distanceTo);


        return targets
                .filter(e -> EntityUtil.isAttackable(e, true)
                        && mc.player.distanceTo(e) <= this.range.getValue()
                        && (mc.player.canSee(e) || !this.raycast.isEnabled()))
                .filter(e -> (EntityUtil.isPlayer(e) && this.players.isEnabled())
                        || (EntityUtil.isMob(e) && this.mobs.isEnabled())
                        || (EntityUtil.isAnimal(e) && this.animals.isEnabled())
                        || (e instanceof ArmorStandEntity && this.armorStands.isEnabled())
                        || ((e instanceof ShulkerBulletEntity || e instanceof AbstractFireballEntity) && this.projectiles.isEnabled()))
                .sorted(comparator)
                .limit( 1L)
                .collect(Collectors.toList());
    }
}
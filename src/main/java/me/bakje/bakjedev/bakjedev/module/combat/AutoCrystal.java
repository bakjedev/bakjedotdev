package me.bakje.bakjedev.bakjedev.module.combat;

import me.bakje.bakjedev.bakjedev.event.events.WorldRenderEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import com.google.common.collect.Streams;
import me.bakje.bakjedev.bakjedev.module.settings.BooleanSetting;
import me.bakje.bakjedev.bakjedev.module.settings.NumberSetting;
import me.bakje.bakjedev.bakjedev.util.InventoryUtil;
import me.bakje.bakjedev.bakjedev.util.render.Renderer;
import me.bakje.bakjedev.bakjedev.util.render.color.QuadColor;
import me.bakje.bakjedev.bakjedev.util.world.DamageUtils;
import me.bakje.bakjedev.bakjedev.util.world.EntityUtil;
import me.bakje.bakjedev.bakjedev.util.world.WorldUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import java.util.HashMap;
import java.util.Map;


public class AutoCrystal extends Mod {
    private BlockPos render = null;
    private int breakCooldown = 0;
    private int placeCooldown = 0;
    private Map<BlockPos, Integer> blacklist = new HashMap<>();
    public BooleanSetting players = new BooleanSetting("Players", true);
    public BooleanSetting mobs = new BooleanSetting("Mobs", false);
    public BooleanSetting animals = new BooleanSetting("Animals", false);
    public BooleanSetting explode = new BooleanSetting("Explode", true);
    public BooleanSetting antiWeakness = new BooleanSetting("AntiWeakness", true);
    public BooleanSetting antiSuicide = new BooleanSetting("AntiSuicide", true);
    public NumberSetting cpte = new NumberSetting("CPT", 1, 10, 2, 1);
    public NumberSetting cooldowne = new NumberSetting("Cooldown", 0, 10, 0, 1);
    public NumberSetting minHealth = new NumberSetting("MinHealth", 0, 20, 2, 1);
    public BooleanSetting place = new BooleanSetting("Place", true);
    public BooleanSetting autoSwitch = new BooleanSetting("AutoSwitch", true);
    public BooleanSetting crystalBlacklist = new BooleanSetting("Blacklist", true);
    public BooleanSetting raycast = new BooleanSetting("RayCast", false);
    public NumberSetting minDamage = new NumberSetting("MinDamage", 1, 20, 2, 1);
    public NumberSetting minRatio = new NumberSetting("MinRatio", 0.5, 6, 2, 0.1);
    public NumberSetting cptp = new NumberSetting("CPT", 1, 10, 2, 1);
    public NumberSetting cooldownp = new NumberSetting("Cooldown", 1, 10, 2, 1);
    public BooleanSetting sameTick = new BooleanSetting("Sametick", false);
    public BooleanSetting rotate = new BooleanSetting("Rotate", false);
    public NumberSetting range = new NumberSetting("Range", 0, 6, 4.5, 0.1);



    public AutoCrystal() {
        super("AutoCrystal", "revolutionary 2020 crystal hacks", Category.COMBAT, true);
        addSettings(players,mobs,animals,explode, antiWeakness, antiSuicide, cpte, cooldowne, minHealth, place, autoSwitch, crystalBlacklist, raycast, minDamage, minRatio, cptp, cooldownp, sameTick, rotate, range);
    }

    @Override
    public void onTick() {
        breakCooldown = Math.max(0, breakCooldown - 1);
        placeCooldown = Math.max(0, placeCooldown - 1);

        for (Entry<BlockPos, Integer> e : new HashMap<>(blacklist).entrySet()) {
            if (e.getValue() > 0) {
                blacklist.replace(e.getKey(), e.getValue() - 1);
            } else {
                blacklist.remove(e.getKey());
            }
        }

        if (mc.player.isUsingItem() && mc.player.getMainHandStack().isFood()) {
            return;
        }

        List<LivingEntity> targets = Streams.stream(mc.world.getEntities())
                .filter(e -> EntityUtil.isAttackable(e, true))
                .filter(e -> (this.players.isEnabled() && EntityUtil.isPlayer(e))
                        || (this.mobs.isEnabled() && EntityUtil.isMob(e))
                        || (this.animals.isEnabled() && EntityUtil.isAnimal(e)))
                .map(e -> (LivingEntity) e)
                .toList();

        if (targets.isEmpty()) {
            return;
        }

        // Explode
        BooleanSetting explodeToggle = this.explode;
        List<EndCrystalEntity> nearestCrystals = Streams.stream(mc.world.getEntities())
                .filter(e -> e instanceof EndCrystalEntity)
                .map(e -> (EndCrystalEntity) e)
                .sorted(Comparator.comparing(mc.player::distanceTo))
                .toList();

        int breaks = 0;
        if (explodeToggle.isEnabled() && !nearestCrystals.isEmpty() && breakCooldown <= 0) {
            boolean end = false;
            for (EndCrystalEntity c : nearestCrystals) {
                if (mc.player.distanceTo(c) > this.range.getValue()
                        || mc.world.getOtherEntities(null, new Box(c.getPos(), c.getPos()).expand(7), targets::contains).isEmpty())
                    continue;

                float damage = DamageUtils.getExplosionDamage(c.getPos(), 6f, mc.player);
                if (DamageUtils.willGoBelowHealth(mc.player, damage, this.minHealth.getValueFloat()))
                    continue;

                int oldSlot = mc.player.getInventory().selectedSlot;
                if (this.antiWeakness.isEnabled() && mc.player.hasStatusEffect(StatusEffects.WEAKNESS)) {
                    InventoryUtil.selectSlot(false, true, Comparator.comparing(i -> DamageUtils.getItemAttackDamage(mc.player.getInventory().getStack(i))));
                }

                if (this.rotate.isEnabled()) {
                    Vec3d eyeVec = mc.player.getEyePos();
                    Vec3d v = new Vec3d(c.getX(), c.getY() + 0.5, c.getZ());
                    for (Direction d : Direction.values()) {
                        Vec3d vd = WorldUtil.getLegitLookPos(c.getBoundingBox(), d, true, 5, -0.001);
                        if (vd != null && eyeVec.distanceTo(vd) <= eyeVec.distanceTo(v)) {
                            v = vd;
                        }
                    }

                    WorldUtil.facePosAuto(v.x, v.y, v.z);
                }

                mc.interactionManager.attackEntity(mc.player, c);
                mc.player.swingHand(Hand.MAIN_HAND);
                blacklist.remove(c.getBlockPos().down());

                InventoryUtil.selectSlot(oldSlot);

                end = true;
                breaks++;
                if (breaks >= this.cpte.getValue()) {
                    break;
                }
            }

            breakCooldown = this.cooldowne.getValueInt() + 1;

            if (!this.sameTick.isEnabled() && end) {
                return;
            }
        }

        // Place
        BooleanSetting placeToggle = this.place;
        if (placeToggle.isEnabled() && placeCooldown <= 0) {
            int crystalSlot = !this.autoSwitch.isEnabled()
                    ? (mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL ? mc.player.getInventory().selectedSlot
                    : mc.player.getOffHandStack().getItem() == Items.END_CRYSTAL ? 40
                    : -1)
                    : InventoryUtil.getSlot(true, i -> mc.player.getInventory().getStack(i).getItem() == Items.END_CRYSTAL);

            if (crystalSlot == -1) {
                return;
            }

            Map<BlockPos, Float> placeBlocks = new LinkedHashMap<>();

            for (Vec3d v : getCrystalPoses()) {
                float playerDamg = DamageUtils.getExplosionDamage(v, 6f, mc.player);

                if (DamageUtils.willKill(mc.player, playerDamg))
                    continue;

                for (LivingEntity e : targets) {
                    float targetDamg = DamageUtils.getExplosionDamage(v, 6f, e);
                    if (DamageUtils.willPop(mc.player, playerDamg) && !DamageUtils.willPopOrKill(e, targetDamg)) {
                        continue;
                    }

                    if (targetDamg >= this.minDamage.getValue()) {
                        float ratio = playerDamg == 0 ? targetDamg : targetDamg / playerDamg;

                        if (ratio > minRatio.getValue()) {
                            placeBlocks.put(new BlockPos(v).down(), ratio);
                        }
                    }
                }
            }

            placeBlocks = placeBlocks.entrySet().stream()
                    .sorted((b1, b2) -> Float.compare(b2.getValue(), b1.getValue()))
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (x, y) -> y, LinkedHashMap::new));

            int oldSlot = mc.player.getInventory().selectedSlot;
            int places = 0;
            for (Entry<BlockPos, Float> e : placeBlocks.entrySet()) {
                BlockPos block = e.getKey();

                Vec3d eyeVec = mc.player.getEyePos();

                Vec3d vec = Vec3d.ofCenter(block, 1);
                Direction dir = null;
                for (Direction d : Direction.values()) {
                    Vec3d vd = WorldUtil.getLegitLookPos(block, d, true, 5);
                    if (vd != null && eyeVec.distanceTo(vd) <= eyeVec.distanceTo(vec)) {
                        vec = vd;
                        dir = d;
                    }
                }

                if (dir == null) {
                    if (this.raycast.isEnabled())
                        continue;

                    dir = Direction.UP;
                }

                if (this.crystalBlacklist.isEnabled())
                    blacklist.put(block, 4);

                if (this.rotate.isEnabled()) {
                    WorldUtil.facePosAuto(vec.x, vec.y, vec.z);
                }

                Hand hand = InventoryUtil.selectSlot(crystalSlot);

                render = block;
                mc.interactionManager.interactBlock(mc.player, hand, new BlockHitResult(vec, dir, block, false));

                places++;
                if (places >= cptp.getValueInt()) {
                    break;
                }
            }

            if (places > 0) {
                if (this.autoSwitch.isEnabled()) {
                    InventoryUtil.selectSlot(oldSlot);
                }

                placeCooldown = cooldownp.getValueInt() + 1;
            }
        }
    }

    @BakjeSubscribe
    public void onRenderWorld(WorldRenderEvent.Post event) {
        if (this.render != null) {
            Renderer.drawBoxBoth(render, QuadColor.single(178, 178, 255, 100), 2.5f);
        }
    }

    public Set<Vec3d> getCrystalPoses() {
        Set<Vec3d> poses = new HashSet<>();

        int range = (int) Math.floor(this.range.getValue());
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos basePos = new BlockPos(mc.player.getEyePos()).add(x, y, z);

                    if (!canPlace(basePos) || (blacklist.containsKey(basePos) && this.crystalBlacklist.isEnabled()))
                        continue;

                    if (this.raycast.isEnabled()) {
                        boolean allBad = true;
                        for (Direction d : Direction.values()) {
                            if (WorldUtil.getLegitLookPos(basePos, d, true, 5) != null) {
                                allBad = false;
                                break;
                            }
                        }

                        if (allBad) {
                            continue;
                        }
                    }

                    if (mc.player.getPos().distanceTo(Vec3d.of(basePos).add(0.5, 1, 0.5)) <= this.range.getValue() + 0.25)
                        poses.add(Vec3d.of(basePos).add(0.5, 1, 0.5));
                }
            }
        }

        return poses;
    }

    private boolean canPlace(BlockPos basePos) {
        BlockState baseState = mc.world.getBlockState(basePos);

        if (baseState.getBlock() != Blocks.BEDROCK && baseState.getBlock() != Blocks.OBSIDIAN)
            return false;

        boolean oldPlace = false;
        BlockPos placePos = basePos.up();
        if (!mc.world.isAir(placePos) || (oldPlace && !mc.world.isAir(placePos.up())))
            return false;

        return mc.world.getOtherEntities(null, new Box(placePos, placePos.up(oldPlace ? 2 : 1))).isEmpty();
    }
}
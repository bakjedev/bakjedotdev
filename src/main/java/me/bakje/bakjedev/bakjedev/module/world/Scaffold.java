package me.bakje.bakjedev.bakjedev.module.world;

import com.google.common.collect.Sets;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.settings.BooleanSetting;
import me.bakje.bakjedev.bakjedev.module.settings.ModeSetting;
import me.bakje.bakjedev.bakjedev.module.settings.NumberSetting;
import me.bakje.bakjedev.bakjedev.util.InventoryUtil;
import me.bakje.bakjedev.bakjedev.util.world.WorldUtil;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashSet;
import java.util.Set;

public class Scaffold extends Mod {
    public ModeSetting area = new ModeSetting("Area","Normal", "Normal", "3x3", "5x5", "7x7");
    public NumberSetting BPT = new NumberSetting("BPT", 1, 10, 2, 1);
    public NumberSetting range = new NumberSetting("Range", 0, 1, 0.3, 0.1);
    public BooleanSetting rotate = new BooleanSetting("Rotate", false);
    public BooleanSetting legit = new BooleanSetting("Legit", false);
    public BooleanSetting tower = new BooleanSetting("Tower", true);
    public BooleanSetting legitTower = new BooleanSetting("LegitTower", false);
    public BooleanSetting airPlace = new BooleanSetting("AirPlace", false);
    public BooleanSetting safeWalk = new BooleanSetting("SafeWalk", true);
    public BooleanSetting noSwing = new BooleanSetting("NoSwing", false);
    public BooleanSetting emptyToggle = new BooleanSetting("EmptyToggle", false);



    public Scaffold() {
        super("Scaffold", "places blocks below you", Category.WORLD, true);
        addSettings(area,BPT,range,rotate,legit,tower,legitTower,airPlace,safeWalk,noSwing,emptyToggle);
    }

    private boolean shouldUseItem(Item item) {
        if (!(item instanceof BlockItem)) {
            return false;
        }

        return true;
    }

    @Override
    public void onTick() {
        int slot = InventoryUtil.getSlot(false, i -> shouldUseItem(mc.player.getInventory().getStack(i).getItem()));

        if (slot ==-1) {
            if (this.emptyToggle.isEnabled()) {
                this.toggle();
            }
            return;
        }

        double range = this.range.getValue();
        int mode = this.area.getIndex();

        Vec3d placeVec = mc.player.getPos().add(0, -0.85, 0);
        Set<BlockPos> blocks = mode == 0
                ? Sets.newHashSet(
                        new BlockPos(placeVec),
                        new BlockPos(placeVec.add(range,0,0)),
                        new BlockPos(placeVec.add(-range,0,0)),
                        new BlockPos(placeVec.add(0,0,range)),
                        new BlockPos(placeVec.add(0,0,-range)))
                        : getSpiral(mode, new BlockPos(placeVec)
        );

        if (this.tower.isEnabled()
        && InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(mc.options.jumpKey.getBoundKeyTranslationKey()).getCode())) {
            if (mc.world.getBlockState(mc.player.getBlockPos().down()).getMaterial().isReplaceable()
            && !mc.world.getBlockState(mc.player.getBlockPos().down(2)).getMaterial().isReplaceable()
            && mc.player.getVelocity().y > 0) {
                mc.player.setVelocity(mc.player.getVelocity().x, -0.1, mc.player.getVelocity().z);

                if (!this.legitTower.isEnabled()) {
                    mc.player.jump();
                }
            }

            if (this.legitTower.isEnabled() && mc.player.isOnGround()) {
                mc.player.jump();
            }
        }

        if (blocks.stream().noneMatch(b -> WorldUtil.isBlockEmpty(b))) {
            return;
        }

        int cap = 0;
        for (BlockPos bp : blocks) {
            boolean placed = WorldUtil.placeBlock(bp, slot, this.legit.isEnabled(), this.airPlace.isEnabled(), !this.noSwing.isEnabled());

            if (placed) {
                cap++;
                if (cap>=this.BPT.getValueInt()) {
                    return;
                }
            }
        }
    }

    private Set<BlockPos> getSpiral(int size, BlockPos center) {
        Set<BlockPos> list = new LinkedHashSet<>();
        list.add(center);

        if (size == 0)
            return list;

        int step = 1;
        int neededSteps = size * 4;
        BlockPos currentPos = center;
        for (int i = 0; i <= neededSteps; i++) {
            // Do 1 less step on the last side to not overshoot the spiral
            if (i == neededSteps)
                step--;

            for (int j = 0; j < step; j++) {
                if (i % 4 == 0) {
                    currentPos = currentPos.add(-1, 0, 0);
                } else if (i % 4 == 1) {
                    currentPos = currentPos.add(0, 0, -1);
                } else if (i % 4 == 2) {
                    currentPos = currentPos.add(1, 0, 0);
                } else {
                    currentPos = currentPos.add(0, 0, 1);
                }

                list.add(currentPos);
            }

            if (i % 2 != 0)
                step++;
        }

        return list;
    }
}

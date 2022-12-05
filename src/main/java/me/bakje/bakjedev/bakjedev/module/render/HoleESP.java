package me.bakje.bakjedev.bakjedev.module.render;

import me.bakje.bakjedev.bakjedev.event.events.WorldRenderEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.settings.BooleanSetting;
import me.bakje.bakjedev.bakjedev.module.settings.NumberSetting;
import me.bakje.bakjedev.bakjedev.util.render.Renderer;
import me.bakje.bakjedev.bakjedev.util.render.color.QuadColor;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HoleESP extends Mod {
    private Map<BlockPos, int[]> holes = new HashMap<>();
    public NumberSetting radius = new NumberSetting("Radius", 1, 20, 10, 1);
    public BooleanSetting bedrock = new BooleanSetting("Bedrock", true);
    public BooleanSetting mixed = new BooleanSetting("Mixed", true);
    public BooleanSetting obsidian = new BooleanSetting("Obsidian", true);


    public HoleESP() {
        super("HoleESP", "holer", Category.RENDER, true);
        addSettings(radius, bedrock, mixed, obsidian);
    }


    @Override
    public void onDisable() {
        holes.clear();
        super.onDisable();
    }


    @Override
    public void onTick() {
        if (mc.player.age % 14 == 0) {
            holes.clear();

            int dist = this.radius.getValueInt();


            for (BlockPos pos : BlockPos.iterateOutwards(mc.player.getBlockPos(), dist, dist, dist)) {
                if (!mc.world.isInBuildLimit(pos.down())
                        || (mc.world.getBlockState(pos.down()).getBlock() != Blocks.BEDROCK
                        && mc.world.getBlockState(pos.down()).getBlock() != Blocks.OBSIDIAN)
                        || !mc.world.getBlockState(pos).getCollisionShape(mc.world, pos).isEmpty()
                        || !mc.world.getBlockState(pos.up(1)).getCollisionShape(mc.world, pos.up(1)).isEmpty()
                        || !mc.world.getBlockState(pos.up(2)).getCollisionShape(mc.world, pos.up(2)).isEmpty()) {
                    continue;
                }

                int bedrockCounter = 0;
                int obsidianCounter = 0;
                for (BlockPos pos1 : neighbours(pos)) {
                    if (mc.world.getBlockState(pos1).getBlock() == Blocks.BEDROCK) {
                        bedrockCounter++;
                    } else if (mc.world.getBlockState(pos1).getBlock() == Blocks.OBSIDIAN) {
                        obsidianCounter++;
                    } else {
                        break;
                    }
                }

                if (bedrockCounter == 5 && this.bedrock.isEnabled()) {
                    int[] bedrockColor = {1, 128, 1};
                    holes.put(pos.toImmutable(), bedrockColor);
                } else if (obsidianCounter == 5 && this.obsidian.isEnabled()) {
                    int[] obsidianColor = {128, 1, 1};
                    holes.put(pos.toImmutable(), obsidianColor);
                } else if (bedrockCounter >= 1 && obsidianCounter >= 1
                        && bedrockCounter + obsidianCounter == 5 && this.mixed.isEnabled()) {
                    int[] mixedColor = {128, 1, 1};
                    holes.put(pos.toImmutable(), mixedColor);
                }
            }
        }
    }

    @BakjeSubscribe
    public void onRender(WorldRenderEvent.Post event) {
            holes.forEach((pos, color) ->
                    Renderer.drawBoxBoth(pos.offset(Direction.DOWN), QuadColor.single(color[0], color[1], color[2], 100), 2.5f));
    }

    private BlockPos[] neighbours(BlockPos pos) {
        return new BlockPos[] {
                pos.west(), pos.east(), pos.south(), pos.north(), pos.down()
        };
    }
}

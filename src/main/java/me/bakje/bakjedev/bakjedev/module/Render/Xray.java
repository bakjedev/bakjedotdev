package me.bakje.bakjedev.bakjedev.module.Render;

import me.bakje.bakjedev.bakjedev.module.Settings.BooleanSetting;
import me.bakje.bakjedev.bakjedev.module.Mod;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;

public class Xray extends Mod {
    public static ArrayList<Block> blocks = new ArrayList<>();
    public BooleanSetting bedrock = new BooleanSetting("Bedrock", false);
    public Xray() {
        super("Xray", "look through the world... and beyond", Category.RENDER, true);
        addSetting(bedrock);
        Registry.BLOCK.forEach(block -> {
            if (isGoodBlock(block)) blocks.add(block);
        });
    }

    boolean isGoodBlock(Block block) {
        boolean c1 = block == Blocks.LAVA || block == Blocks.CHEST || block == Blocks.FURNACE || block == Blocks.END_GATEWAY || block == Blocks.COMMAND_BLOCK || block == Blocks.ANCIENT_DEBRIS || block == Blocks.NETHER_PORTAL || block == Blocks.BEDROCK ||block == Blocks.SPAWNER;
        boolean c2 = block instanceof OreBlock || block instanceof RedstoneOreBlock;
        return c1 || c2;
    }
    @Override
    public void onEnable() {
        mc.options.getGamma().setValue(10d);
        mc.worldRenderer.reload();
        mc.chunkCullingEnabled = false;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.options.getGamma().setValue(1d);
        mc.worldRenderer.reload();
        mc.chunkCullingEnabled = true;
        super.onDisable();
    }
}

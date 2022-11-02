package me.bakje.bakjedev.bakjedev.module.Misc;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.NumberSetting;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;


public class LOHarvest extends Mod {
    public LOHarvest() {
        super("LOharvest", "harvest thing from liveoverflow video", Category.MISC);
    }

    @Override
    public void onTick() {
        for (int y = -1; y <= 0; y++) {
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    tryPlant(mc, mc.player.getBlockPos().add(x, y, z));
                }
            }
        }
        super.onTick();
    }

    public void tryPlant(MinecraftClient client, BlockPos pos) {
        BlockState blockState = client.world.getBlockState(pos);
        if (blockState.getBlock() instanceof FarmlandBlock) {
            BlockState blockStateUp = client.world.getBlockState(pos.up());
            if (blockStateUp.getBlock() instanceof AirBlock) {
                if (tryUseSeed(client, pos, Hand.MAIN_HAND) || tryUseSeed(client, pos, Hand.OFF_HAND)) {
                    //deez
                }
            }
        }
    }

    public boolean tryUseSeed(MinecraftClient client, BlockPos pos, Hand hand) {
        Item item = client.player.getStackInHand(hand).getItem();
        if (item == Items.WHEAT_SEEDS) {
            Vec3d blockPos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
            BlockHitResult hit = new BlockHitResult(blockPos, Direction.UP, pos, false);
            client.interactionManager.interactBlock(client.player, hand, hit);
            return true;
        }
        return false;
    }
}

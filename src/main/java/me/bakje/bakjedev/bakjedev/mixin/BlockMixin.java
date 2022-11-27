package me.bakje.bakjedev.bakjedev.mixin;

import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.render.Xray;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private static void shouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (ModuleManager.INSTANCE.getModule(Xray.class).isEnabled()) {
            if (state.isOf(Blocks.BEDROCK)) {
                if (ModuleManager.INSTANCE.getModule(Xray.class).bedrock.isEnabled()) {
                    cir.setReturnValue(true);
                } else
                    cir.setReturnValue(false);
            } else {
                cir.setReturnValue(Xray.blocks.contains(state.getBlock()));
            }
        }
    }

    @Inject(method = "isTranslucent", at = @At("HEAD"), cancellable = true)
    public void isTranslucent(BlockState state, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (ModuleManager.INSTANCE.getModule(Xray.class).isEnabled()) {
            cir.setReturnValue(!Xray.blocks.contains(state.getBlock()));
        }
    }
}

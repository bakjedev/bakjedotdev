package me.bakje.bakjedev.bakjedev.module.Movement;

import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class NoSlow extends Mod {
    public NoSlow() {
        super("NoSlow", "no eating slowing", Category.MOVEMENT, true);
    }


}

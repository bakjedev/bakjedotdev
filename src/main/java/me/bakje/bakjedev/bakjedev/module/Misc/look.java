package me.bakje.bakjedev.bakjedev.module.Misc;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.NumberSetting;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;


public class look extends Mod {
    double locX = 0;
    double locY = 0;
    double locZ = 0;
    public look() {
        super("look", "testing look", Category.MISC);
    }

    @Override
    public void onTick() {
        double dX = mc.player.getX() - locX;
        double dY = mc.player.getY() - locY;
        double dZ = mc.player.getZ() - locZ;
        double DistanceXZ = Math.sqrt(dX * dX + dZ * dZ);
        double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + dY * dY);
        double yaw = Math.acos(dX / DistanceXZ) * 180 / Math.PI;
        double pitch = Math.acos(dY / -DistanceY) * 180 / Math.PI - 90;
        if (dZ < 0.0)
            yaw = yaw + Math.abs(180 - yaw) * 2;
        yaw = (yaw + 90);

        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), (float) yaw, (float) pitch, mc.player.isOnGround()));
        super.onTick();
    }
}
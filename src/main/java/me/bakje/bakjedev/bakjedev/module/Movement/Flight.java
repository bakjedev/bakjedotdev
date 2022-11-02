package me.bakje.bakjedev.bakjedev.module.Movement;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.BooleanSetting;
import me.bakje.bakjedev.bakjedev.module.Settings.ModeSetting;
import me.bakje.bakjedev.bakjedev.module.Settings.NumberSetting;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Flight extends Mod {

    public ModeSetting flightMode = new ModeSetting("Mode","Vanilla", "Vanilla", "NoEvent");
    public NumberSetting speed = new NumberSetting("Speed", 0, 1, 0.5, 0.1);
    public BooleanSetting antiKick = new BooleanSetting("Anti kick", false);
    public static ClientPlayNetworkHandler networkHandler;
    int antiKickCounter = 0;

    public Flight() {
        super("Flight", "flying like bruh", Category.MOVEMENT);
        addSettings(flightMode, speed, antiKick);
    }


    @Override
    public void onTick() {
        if (flightMode.getMode().equalsIgnoreCase("Vanilla")) {
            if (antiKick.isEnabled()) {
                antiKickCounter++;
                if (antiKickCounter > 20 && mc.player.world.getBlockState(new BlockPos(mc.player.getPos().subtract(0,0.0433D,0))).isAir()) {
                    antiKickCounter = 0;
                    mc.player.setPos(mc.player.getX(), mc.player.getY()-0.0433D, mc.player.getZ());
                }
            }
            mc.player.getAbilities().flying = true;
            mc.player.getAbilities().setFlySpeed((float) speed.getValueFloat());

        } else if (flightMode.getMode().equalsIgnoreCase("NoEvent")) {

        }
        super.onTick();
    }
    @Override
    public void onDisable() {
        mc.player.getAbilities().flying = false;
        super.onDisable();
    }
}

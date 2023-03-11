package me.bakje.bakjedev.bakjedev.module.movement;

import me.bakje.bakjedev.bakjedev.event.events.PacketEvent;
import me.bakje.bakjedev.bakjedev.event.events.WorldRenderEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.settings.BooleanSetting;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public class NoSlow extends Mod {
    public BooleanSetting eating = new BooleanSetting("Eating", true);
    public BooleanSetting inventory = new BooleanSetting("Inventory", true);
    private long lastTime;
    public NoSlow() {
        super("NoSlow", "don't waste time", Category.MOVEMENT, true);
        addSettings(eating, inventory);
    }
    @Override
    public void onTick() {
        if (shouldInvMove(mc.currentScreen) && this.inventory.isEnabled()) {
            for (KeyBinding k : new KeyBinding[] { mc.options.forwardKey, mc.options.backKey,
                    mc.options.leftKey, mc.options.rightKey, mc.options.jumpKey, mc.options.sprintKey }) {
                k.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(k.getBoundKeyTranslationKey()).getCode()));
            }

            mc.options.sneakKey.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(mc.options.sneakKey.getBoundKeyTranslationKey()).getCode()));
        }
    }

    @BakjeSubscribe
    public void onSendPacket(PacketEvent.Send event) {
        if (this.inventory.isEnabled()) {
            if (event.getPacket() instanceof ClickSlotC2SPacket) {
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
            }
        }
    }

    @BakjeSubscribe
    public void onRender(WorldRenderEvent.Post event) {
        if (shouldInvMove(mc.currentScreen) && this.inventory.isEnabled()) {

            float yaw = 0f;
            float pitch = 0f;

//            mc.keyboard.setRepeatEvents(true);

            float amount = (System.currentTimeMillis() - lastTime) / 10f;
            lastTime = System.currentTimeMillis();

            if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT))
                yaw -= amount;
            if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT))
                yaw += amount;
            if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_UP))
                pitch -= amount;
            if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_DOWN))
                pitch += amount;



            mc.player.setYaw(mc.player.getYaw() + yaw);


            mc.player.setPitch(MathHelper.clamp(mc.player.getPitch() + pitch, -90f, 90f));

        }
    }

    private boolean shouldInvMove(Screen screen) {
        if (screen == null) {
            return false;
        }

        return !(screen instanceof ChatScreen
                || screen instanceof BookEditScreen
                || screen instanceof SignEditScreen
                || screen instanceof JigsawBlockScreen
                || screen instanceof StructureBlockScreen
                || screen instanceof AnvilScreen
                || (screen instanceof CreativeInventoryScreen));
    }


}

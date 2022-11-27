package me.bakje.bakjedev.bakjedev.module.movement;

import me.bakje.bakjedev.bakjedev.event.events.PacketEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class GUIMove extends Mod {
    private long lastTime;
    public GUIMove() {
        super("GUIMove", "move in gui's", Category.MOVEMENT, true);
    }

    @Override
    public void onTick() {
        if (shouldInvMove(mc.currentScreen)) {
            for (KeyBinding k : new KeyBinding[] { mc.options.forwardKey, mc.options.backKey,
                    mc.options.leftKey, mc.options.rightKey, mc.options.jumpKey, mc.options.sprintKey }) {
                k.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(k.getBoundKeyTranslationKey()).getCode()));
            }

            mc.options.sneakKey.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(mc.options.sneakKey.getBoundKeyTranslationKey()).getCode()));
        }
    }

    @BakjeSubscribe
    public void onSendPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof ClickSlotC2SPacket) {
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
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
                || (screen instanceof CreativeInventoryScreen
                && ((CreativeInventoryScreen) screen).getSelectedTab() == ItemGroup.SEARCH.getIndex()));
    }


}

package me.bakje.bakjedev.bakjedev.module.Combat;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.BooleanSetting;
import me.bakje.bakjedev.bakjedev.module.Settings.ModeSetting;
import me.bakje.bakjedev.bakjedev.module.Settings.NumberSetting;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;


public class AutoTotem extends Mod {

    private int delay;
    private boolean holdingTotem;
    public NumberSetting delaySetting = new NumberSetting("Delay", 0, 10, 1, 1);
    public BooleanSetting Override = new BooleanSetting("Override", true);

    public AutoTotem() {
        super("AutoTotem", "offhand replenish totem", Category.COMBAT);
        addSettings(delaySetting, Override);
    }

    @Override
    public void onTick() {
        if (holdingTotem && mc.player.getOffHandStack().getItem()!= Items.TOTEM_OF_UNDYING) {
            delay = delaySetting.getValueInt();
        }
        holdingTotem = mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING;

        if (delay > 0) {
            delay--;
            return;
        }

        if (holdingTotem || (!mc.player.getOffHandStack().isEmpty() && !Override.isEnabled())) {
            return;
        }

        if (mc.player.playerScreenHandler == mc.player.currentScreenHandler) {
            for (int i = 9; i < 45; i++) {
                if (mc.player.getInventory().getStack(i >=36 ? i -36 : i).getItem() == Items.TOTEM_OF_UNDYING) {
                    boolean itemInOffhand = !mc.player.getOffHandStack().isEmpty();
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player);

                    if (itemInOffhand)
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);

                    delay = delaySetting.getValueInt();
                    return;
                }
            }
        } else {
            for (int i = 0; i < 9; i++) {
                if (mc.player.getInventory().getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    if (i != mc.player.getInventory().selectedSlot) {
                        mc.player.getInventory().selectedSlot = i;
                        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(i));
                    }

                    mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
                    delay = delaySetting.getValueInt();
                    return;
                }
            }
        }
    }
}
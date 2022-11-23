package me.bakje.bakjedev.bakjedev.module.Combat;

import me.bakje.bakjedev.bakjedev.event.events.SoundPlayEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;


public class PopIdentifier extends Mod {
    public PopIdentifier() {
        super("PopIdentifier", "identify player who pops a totem", Category.COMBAT, true);
    }
    @BakjeSubscribe
    public void onSoundPlay(SoundPlayEvent.Normal event) {
        String path = event.getInstance().getId().getPath();
        if (path.equals("item.totem.use")) {
            for (Entity e : mc.world.getEntities()) {
                if (e instanceof PlayerEntity && e.getPos().equals(new Vec3d(event.getInstance().getX(),event.getInstance().getY(),event.getInstance().getZ()))) {
                    MutableText prefixString = Text.literal("$ ").formatted(Formatting.YELLOW);
                    mc.player.sendMessage(prefixString.append(Text.literal(e.getName().getString()+" popped").formatted(Formatting.GRAY)), false);
                }
            }
        }
    }
}
package me.bakje.bakjedev.bakjedev.module.Render;

import me.bakje.bakjedev.bakjedev.event.events.WorldRenderEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.util.render.Renderer;
import me.bakje.bakjedev.bakjedev.util.render.color.QuadColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.FishEntity;

public class FishESP extends Mod {

    public FishESP() {
        super("FishESP", "what", Category.RENDER, true);
    }

    @BakjeSubscribe
    public void onWorldRender(WorldRenderEvent.Post event) {
        for (Entity e: mc.world.getEntities()) {
            if (e instanceof FishEntity) {
                Renderer.drawBoxOutline(e.getBoundingBox(), QuadColor.single(255, 0, 0, 255), 2);
            }
        }
    }
}

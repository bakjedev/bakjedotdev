package me.bakje.bakjedev.bakjedev.event.events;

import me.bakje.bakjedev.bakjedev.event.Event;
import net.minecraft.client.gl.ShaderEffect;

public class RenderShaderEvent extends Event {
    private ShaderEffect effect;

    public RenderShaderEvent(ShaderEffect effect) {
        this.setEffect(effect);
    }

    public ShaderEffect getEffect() {
        return effect;
    }

    public void setEffect(ShaderEffect effect) {
        this.effect = effect;
    }
}

package me.bakje.bakjedev.bakjedev.mixin;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldRenderer.class)
public interface AccessorWorldRenderer {

    @Accessor
    public abstract Frustum getFrustum();

    @Accessor
    public abstract void setFrustum(Frustum frustum);
}

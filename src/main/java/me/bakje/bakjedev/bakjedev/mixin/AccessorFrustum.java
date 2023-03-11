package me.bakje.bakjedev.bakjedev.mixin;

import org.joml.FrustumIntersection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.Frustum;

@Mixin(Frustum.class)
public interface AccessorFrustum {

    @Accessor
    public abstract FrustumIntersection getFrustumIntersection();

    @Accessor
    public abstract void setFrustumIntersection(FrustumIntersection vector4f);

    @Accessor
    public abstract double getX();

    @Accessor
    public abstract void setX(double x);

    @Accessor
    public abstract double getY();

    @Accessor
    public abstract void setY(double y);

    @Accessor
    public abstract double getZ();

    @Accessor
    public abstract void setZ(double z);
}
package me.bakje.bakjedev.bakjedev.util.render;

import me.bakje.bakjedev.bakjedev.mixin.AccessorFrustum;
import me.bakje.bakjedev.bakjedev.mixin.AccessorWorldRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector4f;

public class FrustumUtil {
    public static Frustum getFrustum() {
        return ((AccessorWorldRenderer) MinecraftClient.getInstance().worldRenderer).getFrustum();
    }

    public static boolean isBoxVisible(Box box) {
        return getFrustum().isVisible(box);
    }

    public static boolean isPointVisible(Vec3d vec) {
        return isPointVisible(vec.x, vec.y, vec.z);
    }

    public static boolean isPointVisible(double x, double y, double z) {
        AccessorFrustum frustum = (AccessorFrustum) getFrustum();
        return frustum.getFrustumIntersection().testPoint((float) (x - frustum.getX()), (float) (y - frustum.getY()), (float) (z - frustum.getZ()));
    }
}

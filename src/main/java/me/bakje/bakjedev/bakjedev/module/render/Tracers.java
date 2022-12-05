package me.bakje.bakjedev.bakjedev.module.render;

import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.event.events.WorldRenderEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.settings.BooleanSetting;
import me.bakje.bakjedev.bakjedev.module.settings.NumberSetting;
import me.bakje.bakjedev.bakjedev.util.render.Renderer;
import me.bakje.bakjedev.bakjedev.util.render.color.LineColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class Tracers extends Mod {
    public NumberSetting width = new NumberSetting("Width", 0.1, 5, 1.5, 0.1);
    public NumberSetting opacity = new NumberSetting("Opacity", 0, 1, 0.40, 0.01);
    private Color distanceColor = new Color(255, 255, 255);
    public BooleanSetting donkey = new BooleanSetting("donkey", false);
    public BooleanSetting creeper = new BooleanSetting("creeper", false);


    public Tracers() {
        super("Tracers", "render distance nocom", Category.RENDER, true);
        addSettings(width,opacity, donkey, creeper);
    }

    @BakjeSubscribe
    public void onRender(WorldRenderEvent.Post event) {


        float width = this.width.getValueFloat();
        int opacity = (int) (this.opacity.getValueFloat() * 255);

        for (Entity e : mc.world.getEntities()) {
            if (e instanceof PlayerEntity && e != mc.player) {

                Vec3d vec = e.getPos().subtract(Renderer.getInterpolationOffset(e));
                Vec3d vec2 = new Vec3d(0, 0, 75)
                        .rotateX(-(float) Math.toRadians(mc.gameRenderer.getCamera().getPitch()))
                        .rotateY(-(float) Math.toRadians(mc.gameRenderer.getCamera().getYaw()))
                        .add(mc.cameraEntity.getEyePos());

                LineColor lineColor = LineColor.single(getColorFromDistance(e).getRed(), getColorFromDistance(e).getGreen(), getColorFromDistance(e).getBlue(), opacity);
                Renderer.drawLine(vec2.x, vec2.y, vec2.z, vec.x, vec.y+mc.player.getHeight()/2, vec.z, lineColor, width);
            }
            if (e instanceof DonkeyEntity && this.donkey.isEnabled()) {

                Vec3d vec = e.getPos().subtract(Renderer.getInterpolationOffset(e));
                Vec3d vec2 = new Vec3d(0, 0, 75)
                        .rotateX(-(float) Math.toRadians(mc.gameRenderer.getCamera().getPitch()))
                        .rotateY(-(float) Math.toRadians(mc.gameRenderer.getCamera().getYaw()))
                        .add(mc.cameraEntity.getEyePos());

                LineColor lineColor = LineColor.single(getColorFromDistance(e).getRed(), getColorFromDistance(e).getGreen(), getColorFromDistance(e).getBlue(), opacity);
                Renderer.drawLine(vec2.x, vec2.y, vec2.z, vec.x, vec.y+mc.player.getHeight()/2, vec.z, lineColor, width);
            }
            if (e instanceof CreeperEntity && this.creeper.isEnabled()) {

                Vec3d vec = e.getPos().subtract(Renderer.getInterpolationOffset(e));
                Vec3d vec2 = new Vec3d(0, 0, 75)
                        .rotateX(-(float) Math.toRadians(mc.gameRenderer.getCamera().getPitch()))
                        .rotateY(-(float) Math.toRadians(mc.gameRenderer.getCamera().getYaw()))
                        .add(mc.cameraEntity.getEyePos());

                LineColor lineColor = LineColor.single(getColorFromDistance(e).getRed(), getColorFromDistance(e).getGreen(), getColorFromDistance(e).getBlue(), opacity);
                Renderer.drawLine(vec2.x, vec2.y, vec2.z, vec.x, vec.y+mc.player.getHeight()/2, vec.z, lineColor, width);
            }
        }
    }

    private Color getColorFromDistance(Entity entity) {
        if (Bakjedev.friendMang.has(entity)) return new Color(85, 85, 255);

        // Credit to meteor client credit to Icy from Stackoverflow
        double distance = mc.player.distanceTo(entity);
        double percent = distance / 60;

        if (percent < 0 || percent > 1) {
            distanceColor = new Color(0, 255, 0, 255);
            return distanceColor;
        }

        int r, g;

        if (percent < 0.5) {
            r = 255;
            g = (int) (255 * percent / 0.5);  // Closer to 0.5, closer to yellow (255,255,0)
        }
        else {
            g = 255;
            r = 255 - (int) (255 * (percent - 0.5) / 0.5); // Closer to 1.0, closer to green (0,255,0)
        }

        distanceColor= new Color(r, g, 0);
        return distanceColor;
    }
}

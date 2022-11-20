package me.bakje.bakjedev.bakjedev.event.events;

import me.bakje.bakjedev.bakjedev.event.Event;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;

public class SkyRenderEvent extends Event {
    public static class Properties extends SkyRenderEvent {

        private DimensionEffects sky;

        public Properties(DimensionEffects sky) {
            this.setSky(sky);
        }

        public DimensionEffects getSky() {
            return sky;
        }

        public void setSky(DimensionEffects sky) {
            this.sky = sky;
        }
    }

    public static class Color extends SkyRenderEvent {

        private float tickDelta;
        private Vec3d color = null;

        public Color(float tickDelta) {
            this.tickDelta = tickDelta;
        }

        public float getTickDelta() {
            return tickDelta;
        }

        public void setColor(Vec3d color) {
            this.color = color;
        }

        public Vec3d getColor() {
            return color;
        }

        public static class SkyColor extends Color {

            public SkyColor(float tickDelta) {
                super(tickDelta);
            }
        }

        public static class CloudColor extends Color {

            public CloudColor(float tickDelta) {
                super(tickDelta);
            }
        }

        public static class FogColor extends Color {

            public FogColor(float tickDelta) {
                super(tickDelta);
            }
        }

        public static class EndSkyColor extends Color {

            public EndSkyColor(float tickDelta) {
                super(tickDelta);
            }
        }
    }
}

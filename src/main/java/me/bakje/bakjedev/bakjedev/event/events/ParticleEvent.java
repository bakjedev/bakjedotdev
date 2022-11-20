package me.bakje.bakjedev.bakjedev.event.events;

import me.bakje.bakjedev.bakjedev.event.Event;
import net.minecraft.client.particle.Particle;
import net.minecraft.particle.ParticleEffect;

public class ParticleEvent extends Event {
    public static class Normal extends ParticleEvent {

        private Particle particle;

        public Normal(Particle particle) {
            this.particle = particle;
        }

        public Particle getParticle() {
            return particle;
        }
    }

    public static class Emitter extends ParticleEvent {

        private ParticleEffect effect;

        public Emitter(ParticleEffect effect) {
            this.effect = effect;
        }

        public ParticleEffect getEffect() {
            return effect;
        }
    }
}

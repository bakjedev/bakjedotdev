package me.bakje.bakjedev.bakjedev.mixin;

import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.event.events.ParticleEvent;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void addParticle(Particle particle, CallbackInfo callback) {
        ParticleEvent.Normal event = new ParticleEvent.Normal(particle);
        Bakjedev.INSTANCE.eventBus.post(event);

        if (event.isCancelled()) {
            callback.cancel();
        }
    }

    @Inject(method = "addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft/particle/ParticleEffect;)V", at = @At("HEAD"), cancellable = true)
    private void addEmitter(Entity entity, ParticleEffect particleEffect, CallbackInfo callback) {
        ParticleEvent.Emitter event = new ParticleEvent.Emitter(particleEffect);
        Bakjedev.INSTANCE.eventBus.post(event);

        if (event.isCancelled()) {
            callback.cancel();
        }
    }

    @Inject(method = "addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft/particle/ParticleEffect;I)V", at = @At("HEAD"), cancellable = true)
    private void addEmitter_(Entity entity, ParticleEffect particleEffect, int maxAge, CallbackInfo callback) {
        ParticleEvent.Emitter event = new ParticleEvent.Emitter(particleEffect);
        Bakjedev.INSTANCE.eventBus.post(event);

        if (event.isCancelled()) {
            callback.cancel();
        }
    }
}

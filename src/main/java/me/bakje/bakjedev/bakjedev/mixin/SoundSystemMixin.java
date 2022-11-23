package me.bakje.bakjedev.bakjedev.mixin;

import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.event.events.SoundPlayEvent;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.TickableSoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {
    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"), cancellable = true)
    private void play(SoundInstance soundInstance, CallbackInfo ci) {
        SoundPlayEvent.Normal event = new SoundPlayEvent.Normal(soundInstance);
        Bakjedev.INSTANCE.eventBus.post(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;I)V", at = @At("HEAD"), cancellable = true)
    private void play(SoundInstance soundInstance, int delay, CallbackInfo ci) {
        SoundPlayEvent.Normal event = new SoundPlayEvent.Normal(soundInstance);
        Bakjedev.INSTANCE.eventBus.post(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "playNextTick", at = @At("HEAD"), cancellable = true)
    private void playNextTick(TickableSoundInstance sound, CallbackInfo ci) {
        SoundPlayEvent.Normal event = new SoundPlayEvent.Normal(sound);
        Bakjedev.INSTANCE.eventBus.post(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "addPreloadedSound", at = @At("HEAD"), cancellable = true)
    private void addPreloadedSound(Sound sound, CallbackInfo ci) {
        SoundPlayEvent.Preloaded event = new SoundPlayEvent.Preloaded(sound);
        Bakjedev.INSTANCE.eventBus.post(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}

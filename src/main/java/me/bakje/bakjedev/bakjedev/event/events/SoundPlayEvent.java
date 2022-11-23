package me.bakje.bakjedev.bakjedev.event.events;

import me.bakje.bakjedev.bakjedev.event.Event;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundInstance;

public class SoundPlayEvent extends Event {
    public static class Normal extends SoundPlayEvent {

        private SoundInstance instance;

        public Normal(SoundInstance si) {
            instance = si;
        }

        public SoundInstance getInstance() {
            return instance;
        }
    }

    public static class Preloaded extends SoundPlayEvent {

        private Sound sound;

        public Preloaded(Sound s) {
            sound = s;
        }

        public Sound getSound() {
            return sound;
        }
    }
}

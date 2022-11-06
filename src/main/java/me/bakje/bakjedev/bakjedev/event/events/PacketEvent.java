package me.bakje.bakjedev.bakjedev.event.events;

import me.bakje.bakjedev.bakjedev.event.Event;
import net.minecraft.network.Packet;

public class PacketEvent extends Event {
    private Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public static class Read extends PacketEvent {

        public Read(Packet<?> packet) {
            super(packet);
        }

    }

    public static class Send extends PacketEvent {

        public Send(Packet<?> packet) {
            super(packet);
        }

    }
}

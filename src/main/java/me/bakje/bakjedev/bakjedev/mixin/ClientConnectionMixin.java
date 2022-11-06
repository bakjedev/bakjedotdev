package me.bakje.bakjedev.bakjedev.mixin;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.event.events.PacketEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Shadow
    private Channel channel;

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo callback) {
        if (this.channel.isOpen() && packet != null) {
            PacketEvent.Read event = new PacketEvent.Read(packet);
            Bakjedev.INSTANCE.eventBus.post(event);

            if (event.isCancelled()) {
                callback.cancel();
            }
        }
    }

    @Inject(method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at = @At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, PacketCallbacks packetCallback, CallbackInfo callback) {
        PacketEvent.Send event = new PacketEvent.Send(packet);
        Bakjedev.INSTANCE.eventBus.post(event);

        if (event.isCancelled()) {
            callback.cancel();
        }
    }
}

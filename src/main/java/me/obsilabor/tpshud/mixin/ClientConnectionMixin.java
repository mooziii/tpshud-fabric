package me.obsilabor.tpshud.mixin;

import me.obsilabor.alert.EventManager;
import me.obsilabor.tpshud.event.PacketReceiveEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener listener, CallbackInfo info) {
        PacketReceiveEvent event = new PacketReceiveEvent(packet);
        EventManager.callEvent(event);
        if(event.isCancelled()) {
            info.cancel();
        }
    }

}

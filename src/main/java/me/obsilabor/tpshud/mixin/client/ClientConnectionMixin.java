package me.obsilabor.tpshud.mixin.client;

import me.obsilabor.tpshud.TpsTracker;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class ClientConnectionMixin {
    @Inject(method = "genericsFtw", at = @At("HEAD"))
    private static <T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener packetListener, CallbackInfo ci) {
        if (packet instanceof ClientboundSetTimePacket timePacket) {
            TpsTracker.INSTANCE.onPacketReceive(timePacket);
        }
    }
}

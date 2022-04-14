package me.obsilabor.tpshud.event;

import me.obsilabor.alert.Cancellable;
import net.minecraft.network.Packet;

public class PacketReceiveEvent extends Cancellable {

    private final Packet<?> packet;

    public PacketReceiveEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}

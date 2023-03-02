package me.obsilabor.tpshud.event

import me.obsilabor.alert.Cancellable
import net.minecraft.network.packet.Packet

class PacketReceiveEvent(val packet: Packet<*>) : Cancellable()

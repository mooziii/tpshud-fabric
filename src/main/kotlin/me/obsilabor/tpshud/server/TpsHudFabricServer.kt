package me.obsilabor.tpshud.server

import me.obsilabor.tpshud.mixininterface.IMinecraftServer
import me.obsilabor.tpshud.networking.Packets
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import java.util.*

class TpsHudFabricServer : DedicatedServerModInitializer {
    override fun onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register {
            Timer().scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    for (player in it.playerManager.playerList) {
                        val byteBuf = PacketByteBufs.create()
                        byteBuf.writeDouble((it as IMinecraftServer).tps)
                        ServerPlayNetworking.send(player, Packets.TPS, byteBuf)
                    }
                    (it as IMinecraftServer).setTPS(0.0)
                }
            }, 0, 20)
        }
        ServerPlayConnectionEvents.JOIN.register { _, connection, _ ->
            val packet = connection.createPacket(Packets.HANDSHAKE, PacketByteBufs.create())
            connection.sendPacket(packet)
        }
    }
}
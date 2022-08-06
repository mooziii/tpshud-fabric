package me.obsilabor.tpshud

import kotlinx.serialization.json.Json
import me.obsilabor.alert.EventManager
import me.obsilabor.tpshud.mixininterface.IMinecraftServer
import me.obsilabor.tpshud.networking.NetworkingListener
import me.obsilabor.tpshud.networking.Packets
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.silkmc.silk.core.kotlin.ticks
import net.silkmc.silk.core.task.infiniteMcCoroutineTask
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

val json = Json {
    prettyPrint = true
    encodeDefaults = true
}

val minecraft: MinecraftClient
    get() = MinecraftClient.getInstance()

class TpsHud : ClientModInitializer {
    override fun onInitializeClient() {
        NetworkingListener
        EventManager.registerListener(TpsTracker.INSTANCE)
    }
}

class TpsHudServer : DedicatedServerModInitializer {
    override fun onInitializeServer() {
        if (!FabricLoader.getInstance().isModLoaded("silk-core")) {
            val logger = LoggerFactory.getLogger("tpshud")
            logger.error("Silk is not installed. tps-hud requires Silk to be installed on the server side.")
            logger.error("---------------------------------------------------------------------------------")
            logger.error("Download Silk:")
            logger.error("Maven Repository: https://repo.maven.apache.org/maven2/net/silkmc/silk-core/")
            logger.error("Modrinth: https://modrinth.com/mod/silk/")
            logger.error("CurseForge: https://www.curseforge.com/minecraft/mc-mods/silk-kt/")
            exitProcess(0)
        }
        ServerLifecycleEvents.SERVER_STARTED.register {
            infiniteMcCoroutineTask(
                sync = false,
                period = 20.ticks
            ) {
                for (player in it.playerManager.playerList) {
                    val byteBuf = PacketByteBufs.create()
                    byteBuf.writeDouble((it as IMinecraftServer).tps)
                    ServerPlayNetworking.send(player, Packets.TPS, byteBuf)
                }
                (it as IMinecraftServer).setTPS(0.0)
            }
        }
        ServerPlayConnectionEvents.JOIN.register { _, connection, _ ->
            val packet = connection.createPacket(Packets.HANDSHAKE, PacketByteBufs.create())
            connection.sendPacket(packet)
        }
    }
}
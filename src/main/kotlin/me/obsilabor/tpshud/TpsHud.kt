package me.obsilabor.tpshud

import kotlinx.serialization.json.Json
import me.obsilabor.alert.EventManager
import me.obsilabor.tpshud.networking.NetworkingListener
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient

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


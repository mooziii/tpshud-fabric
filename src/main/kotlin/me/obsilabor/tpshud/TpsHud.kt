package me.obsilabor.tpshud

import kotlinx.serialization.json.Json
import me.obsilabor.alert.EventManager
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient

val json = Json {
    prettyPrint = true
    encodeDefaults = true
    @Suppress("EXPERIMENTAL_API_USAGE")
    prettyPrintIndent = "  "
}

val minecraft: MinecraftClient
    get() = MinecraftClient.getInstance()

class TpsHud : ClientModInitializer {

    override fun onInitializeClient() {
        EventManager.registerListener(TpsTracker.INSTANCE)
    }
}
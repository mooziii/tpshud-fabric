package me.obsilabor.tpshud.networking

import me.obsilabor.tpshud.TpsTracker
import me.obsilabor.tpshud.config.ClothConfigManager
import me.obsilabor.tpshud.minecraft
import me.obsilabor.tpshud.screen.CompatibleServerScreen
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.toast.SystemToast
import net.minecraft.text.Text

object NetworkingListener {
    init {
        ClientPlayNetworking.registerGlobalReceiver(Packets.HANDSHAKE) { client, _, _, _ ->
            if (ClothConfigManager.config?.askedForServerProvidedData != true) {
                minecraft.execute {
                    client.setScreen(CompatibleServerScreen())
                }
            } else {
                minecraft.execute {
                    client.toastManager.add(SystemToast(
                        SystemToast.Type.TUTORIAL_HINT,
                        Text.translatable("screen.useServerProvidedData.title"),
                        Text.translatable("toast.useServerProvidedData.message")
                    ))
                }
            }
        }
        ClientPlayNetworking.registerGlobalReceiver(Packets.TPS) { _, _, bytebuf, _ ->
            if (ClothConfigManager.config?.askedForServerProvidedData != true) {
                return@registerGlobalReceiver
            }
            val tps = bytebuf.readDouble().toFloat()
            TpsTracker.INSTANCE.serverProvidedTps = tps
        }
    }
}
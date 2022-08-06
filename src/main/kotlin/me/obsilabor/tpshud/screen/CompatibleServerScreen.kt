package me.obsilabor.tpshud.screen

import it.unimi.dsi.fastutil.booleans.BooleanConsumer
import me.obsilabor.tpshud.config.ClothConfigManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ConfirmScreen
import net.minecraft.text.Text

class CompatibleServerScreen : ConfirmScreen(
    BooleanConsumer {
        ClothConfigManager.config?.useServerProvidedData = it
        ClothConfigManager.saveConfigToFile()
        MinecraftClient.getInstance().setScreen(null)
    },
    Text.translatable("screen.useServerProvidedData.title"),
    Text.translatable("screen.useServerProvidedData.message"),
    Text.translatable("screen.useServerProvidedData.yes"),
    Text.translatable("screen.useServerProvidedData.no")

) {
    init {
        ClothConfigManager.config?.askedForServerProvidedData = true
        ClothConfigManager.saveConfigToFile()
    }
}
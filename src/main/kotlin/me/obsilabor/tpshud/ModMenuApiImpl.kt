package me.obsilabor.tpshud

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import com.terraformersmc.modmenu.gui.ModsScreen
import me.obsilabor.tpshud.config.ConfigManager
import me.obsilabor.tpshud.config.platform.YACLConfigPlatform
import me.obsilabor.tpshud.screen.ConfigLibMissingScreen
import net.fabricmc.loader.api.FabricLoader

class ModMenuApiImpl : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory {
            if (FabricLoader.getInstance().isModLoaded("yet-another-config-lib")) {
                YACLConfigPlatform.buildScreen(ModsScreen(null))
            } else {
                ConfigLibMissingScreen()
            }
        }
    }
}
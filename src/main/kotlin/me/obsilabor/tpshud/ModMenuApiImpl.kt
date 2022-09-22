package me.obsilabor.tpshud

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import com.terraformersmc.modmenu.gui.ModsScreen
import me.obsilabor.tpshud.config.ConfigManager
import me.obsilabor.tpshud.config.platform.YACLConfigPlatform

class ModMenuApiImpl : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory {
            YACLConfigPlatform.buildScreen(ModsScreen(null))
        }
    }
}
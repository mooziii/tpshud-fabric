package me.obsilabor.tpshud

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.obsilabor.tpshud.config.ClothConfigManager

class ModMenuApiImpl : ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory {
            ClothConfigManager.buildScreen()
        }
    }

}
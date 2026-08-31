package me.obsilabor.tpshud;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.obsilabor.tpshud.config.platform.YACLConfigPlatform;
import me.obsilabor.tpshud.screen.ConfigLibMissingScreen;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3")) {
                return YACLConfigPlatform.buildScreen(parent);
            } else {
                return new ConfigLibMissingScreen(parent);
            }
        };
    }
}

package me.obsilabor.tpshud.screen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import me.obsilabor.tpshud.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.Component;

public class CompatibleServerScreen extends ConfirmScreen {
    public CompatibleServerScreen() {
        super(
            createCallback(),
            Component.translatable("screen.useServerProvidedData.title"),
            Component.translatable("screen.useServerProvidedData.message"),
            Component.translatable("screen.useServerProvidedData.yes"),
            Component.translatable("screen.useServerProvidedData.no")
        );
        ConfigManager.getConfig().askedForServerProvidedData = true;
        ConfigManager.saveConfig();
    }

    private static BooleanConsumer createCallback() {
        return (choice) -> {
            ConfigManager.getConfig().useServerProvidedData = choice;
            ConfigManager.saveConfig();
            Minecraft.getInstance().setScreenAndShow(null);
        };
    }
}

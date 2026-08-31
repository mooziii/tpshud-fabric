package me.obsilabor.tpshud;

import me.obsilabor.tpshud.config.ConfigManager;
import me.obsilabor.tpshud.hud.TpsWidget;
import me.obsilabor.tpshud.networking.NetworkingListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;

public class TpsHud implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Initialize config
        ConfigManager.init();

        // Initialize networking
        NetworkingListener.init();

        // Initialize TPS tracker
        TpsTracker.init();

        // Register HUD rendering using Fabric API
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.parse("tpshud:tps"), TpsWidget::render);
    }
}

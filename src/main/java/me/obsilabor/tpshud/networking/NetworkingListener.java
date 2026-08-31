package me.obsilabor.tpshud.networking;

import me.obsilabor.tpshud.TpsTracker;
import me.obsilabor.tpshud.config.ConfigManager;
import me.obsilabor.tpshud.screen.CompatibleServerScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;

public class NetworkingListener {
    public static void init() {
        // Register payload types
        PayloadTypeRegistry.clientboundPlay().register(CommonHandshakePayload.TYPE, CommonHandshakePayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(CommonTickRatePayload.TYPE, CommonTickRatePayload.CODEC);

        // Register handshake receiver
        ClientPlayNetworking.registerGlobalReceiver(CommonHandshakePayload.TYPE, (payload, context) -> {
            Minecraft client = context.client();
            if (!ConfigManager.getConfig().askedForServerProvidedData) {
                client.execute(() -> {
                    client.setScreenAndShow(new CompatibleServerScreen());
                });
            } else {
                client.execute(() -> {
                    client.gui.toastManager().addToast(new SystemToast(
                        SystemToast.SystemToastId.NARRATOR_TOGGLE,
                        Component.translatable("screen.useServerProvidedData.title"),
                        Component.translatable("toast.useServerProvidedData.message")
                    ));
                });
            }
        });

        // Register tick rate receiver
        ClientPlayNetworking.registerGlobalReceiver(CommonTickRatePayload.TYPE, (payload, context) -> {
            if (!ConfigManager.getConfig().askedForServerProvidedData) {
                return;
            }
            TpsTracker.INSTANCE.serverProvidedTps = (float) payload.tickRate();
        });
    }
}

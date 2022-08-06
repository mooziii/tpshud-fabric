package me.obsilabor.tpshud.mixin.client;

import me.obsilabor.alert.EventManager;
import me.obsilabor.tpshud.event.GameJoinEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(at = @At("TAIL"), method = "onGameJoin")
    private void triggerJoinEvent(GameJoinS2CPacket packet, CallbackInfo info) {
        EventManager.callEvent(new GameJoinEvent());
    }
}

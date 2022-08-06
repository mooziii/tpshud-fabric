package me.obsilabor.tpshud.mixin.client;

import me.obsilabor.tpshud.hud.TpsWidget;
import me.obsilabor.tpshud.screen.PositionSelectionScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    private void renderTpsHud(MatrixStack matrices, float f, CallbackInfo ci) {
        if(!(MinecraftClient.getInstance().currentScreen instanceof PositionSelectionScreen)) {
            MatrixStack matrixStack = new MatrixStack();
            TpsWidget.INSTANCE.render(matrixStack);
        }
    }
}

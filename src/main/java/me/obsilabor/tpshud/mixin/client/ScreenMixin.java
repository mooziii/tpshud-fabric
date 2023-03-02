package me.obsilabor.tpshud.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import me.obsilabor.tpshud.hud.TpsWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractParentElement implements Drawable {
    @Shadow @Final protected Text title;

    @Shadow @Nullable protected MinecraftClient client;
    @Unique private static final Identifier LIVE_PREVIEW_TEXTURE = new Identifier("tpshud", "textures/live_preview.png");

    @Inject(method = "render", at = @At("HEAD"))
    private void renderLivePreview(MatrixStack matrixStack, int i, int j, float f, CallbackInfo ci) {
        if(title.toString().contains("title.tpshud.config")) {
            var window = client.getWindow();
            final var x = 8;
            final var y = window.getScaledHeight()/2-window.getScaledHeight()/4;
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderTexture(0, LIVE_PREVIEW_TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            drawTexture(matrixStack, x, y, 0.0F, 0.0F, 300, 150, 300, 150);
            matrixStack.push();
            TpsWidget.INSTANCE.renderLivePreview(matrixStack, x+2, y+2);
            matrixStack.pop();
        }
    }
}

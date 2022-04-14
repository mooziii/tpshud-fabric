package me.obsilabor.tpshud.mixin;

import me.obsilabor.tpshud.pos.PositionSelectionScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractParentElement implements Drawable {

    @Shadow @Final protected Text title;

    @Shadow @Final private List<Drawable> drawables;

    @Shadow @Final private List<Element> children;

    @Shadow @Final private List<Selectable> selectables;

    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("TAIL"))
    private void addDragButton(CallbackInfo ci) {
        if(title.toString().contains("tpshud")) {
            ButtonWidget button = new ButtonWidget(5, 5, 150, 20, Text.of("Drag HUD.."), (notUsed) -> {
                MinecraftClient.getInstance().setScreen(new PositionSelectionScreen());
            });
            drawables.add(button);
            children.add(button);
            selectables.add(button);
        }
    }

}

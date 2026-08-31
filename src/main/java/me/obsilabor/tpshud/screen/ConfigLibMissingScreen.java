package me.obsilabor.tpshud.screen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigLibMissingScreen extends Screen {
    private final Screen parent;

    public ConfigLibMissingScreen() {
        this(null);
    }

    public ConfigLibMissingScreen(Screen parent) {
        super(Component.translatable("screen.configLibMissing.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        StringWidget widget1 = new StringWidget(this.title, this.font);
        widget1.setPosition(this.width / 2, this.height / 2 - 10);
        StringWidget widget2 = new StringWidget(Component.translatable("screen.configLibMissing.message"), this.font);
        widget2.setPosition(this.width / 2, this.height / 2 - 5);

        this.addRenderableWidget(widget1);

        this.addRenderableWidget(widget2);

        this.addRenderableWidget(
                Button.builder(
                        Component.translatable("gui.done"),
                        button -> {
                            this.minecraft.gui.setScreen(this.parent);
                        }
                ).bounds(
                        this.width / 2 - 100,
                        this.height / 2 + 20,
                        200,
                        20
                ).build()
        );
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.parent);
    }
}
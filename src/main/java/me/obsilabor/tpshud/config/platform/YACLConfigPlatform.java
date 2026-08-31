package me.obsilabor.tpshud.config.platform;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import me.obsilabor.tpshud.config.ConfigManager;
import me.obsilabor.tpshud.screen.PositionSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.Color;

public class YACLConfigPlatform {
    public static Screen buildScreen(Screen parent) {
        YetAnotherConfigLib.Builder builder = YetAnotherConfigLib.createBuilder()
            .title(Component.translatable("title.tpshud.config"))
            .save(ConfigManager::saveConfig);

        // Style Category
        ConfigCategory styleCategory = ConfigCategory.createBuilder()
            .name(Component.translatable("category.style"))
            .tooltip(Component.translatable("category.style.tooltip"))
            .group(createAppearanceGroup())
            .group(createTextGroup())
            .group(createBackgroundGroup())
            .build();

        // Networking Category
        ConfigCategory networkingCategory = ConfigCategory.createBuilder()
            .name(Component.translatable("category.networking"))
            .tooltip(Component.translatable("category.networking.tooltip"))
            .option(Option.<Boolean>createBuilder()
                .name(Component.translatable("option.tpshud.useServerProvidedData"))
                .description(OptionDescription.of(
                    Component.translatable("option.tpshud.useServerProvidedData.tooltip1"),
                    Component.translatable("option.tpshud.useServerProvidedData.tooltip2")
                ))
                .binding(true,
                    () -> ConfigManager.getConfig().useServerProvidedData,
                    value -> ConfigManager.getConfig().useServerProvidedData = value)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                .build())
            .option(Option.<Boolean>createBuilder()
                .name(Component.translatable("option.tpshud.showCompatibilityToast"))
                .description(OptionDescription.of(Component.translatable("option.tpshud.showCompatibilityToast.tooltip")))
                .binding(true,
                    () -> ConfigManager.getConfig().showCompatibilityToast,
                    value -> ConfigManager.getConfig().showCompatibilityToast = value)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                .build())
            .build();

        builder.category(styleCategory);
        builder.category(networkingCategory);

        return builder.build().generateScreen(parent);
    }

    private static OptionGroup createAppearanceGroup() {
        return OptionGroup.createBuilder()
            .name(Component.translatable("category.style.group.appearance"))
            .option(Option.<Boolean>createBuilder()
                .name(Component.translatable("option.tpshud.enabled"))
                .binding(true,
                    () -> ConfigManager.getConfig().isEnabled,
                    value -> ConfigManager.getConfig().isEnabled = value)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                .build())
            .option(ButtonOption.createBuilder()
                .name(Component.translatable("option.tpshud.dragHud"))
                .description(OptionDescription.of(Component.translatable("option.tpshud.dragHud.tooltip")))
                .action((settingsScreen, button) -> {
                    Minecraft.getInstance().setScreenAndShow(new PositionSelectionScreen(settingsScreen));
                })
                .build())
            .option(Option.<Float>createBuilder()
                .name(Component.translatable("option.tpshud.scale"))
                .binding(1.0f,
                    () -> ConfigManager.getConfig().scale,
                    value -> ConfigManager.getConfig().scale = value)
                .controller(opt -> FloatSliderControllerBuilder.create(opt).range(0.1f, 5.0f).step(0.1f))
                .build())
            .build();
    }

    private static OptionGroup createTextGroup() {
        return OptionGroup.createBuilder()
            .name(Component.translatable("category.style.group.text"))
            .option(Option.<Color>createBuilder()
                .name(Component.translatable("option.tpshud.textColor"))
                .binding(new Color(0xFFFFFF),
                    () -> new Color(ConfigManager.getConfig().textColor),
                    value -> ConfigManager.getConfig().textColor = value.getRGB())
                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(false))
                .build())
            .option(Option.<Color>createBuilder()
                .name(Component.translatable("option.tpshud.valueTextColor"))
                .binding(new Color(0x0778D8),
                    () -> new Color(ConfigManager.getConfig().valueTextColor),
                    value -> ConfigManager.getConfig().valueTextColor = value.getRGB())
                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(false))
                .build())
            .option(Option.<Boolean>createBuilder()
                .name(Component.translatable("option.tpshud.textShadow"))
                .description(OptionDescription.of(Component.translatable("option.tpshud.textShadow.tooltip")))
                .binding(true,
                    () -> ConfigManager.getConfig().textShadow,
                    value -> ConfigManager.getConfig().textShadow = value)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                .build())
            .option(Option.<Boolean>createBuilder()
                .name(Component.translatable("option.tpshud.satisfyTpsCount"))
                .description(OptionDescription.of(Component.translatable("option.tpshud.satisfyTpsCount.tooltip")))
                .binding(true,
                    () -> ConfigManager.getConfig().satisfyTpsCount,
                    value -> ConfigManager.getConfig().satisfyTpsCount = value)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                .build())
            .option(Option.<String>createBuilder()
                .name(Component.translatable("option.tpshud.customText"))
                .description(OptionDescription.of(Component.translatable("option.tpshud.customText.tooltip")))
                .binding("",
                    () -> ConfigManager.getConfig().customText,
                    value -> ConfigManager.getConfig().customText = value)
                .controller(StringControllerBuilder::create)
                .build())
            .option(Option.<Boolean>createBuilder()
                .name(Component.translatable("option.tpshud.displayMode"))
                .description(OptionDescription.of(Component.translatable("option.tpshud.displayMode.tooltip")))
                .binding(true,
                    () -> ConfigManager.getConfig().displayModeTps,
                    value -> ConfigManager.getConfig().displayModeTps = value)
                .controller(opt -> BooleanControllerBuilder.create(opt)
                    .formatValue(value -> value ?
                        Component.translatable("option.tpshud.displayMode.tps") :
                        Component.translatable("option.tpshud.displayMode.mspt"))
                    .coloured(true))
                .build())
            .build();
    }

    private static OptionGroup createBackgroundGroup() {
        return OptionGroup.createBuilder()
            .name(Component.translatable("category.style.group.background"))
            .option(Option.<Color>createBuilder()
                .name(Component.translatable("option.tpshud.backgroundColor"))
                .binding(new Color(0xFFFFFF),
                    () -> new Color(ConfigManager.getConfig().backgroundColor),
                    value -> ConfigManager.getConfig().backgroundColor = value.getRGB())
                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(false))
                .build())
            .option(Option.<Float>createBuilder()
                .name(Component.translatable("option.tpshud.backgroundOpacity"))
                .binding(0.5f,
                    () -> ConfigManager.getConfig().backgroundOpacity,
                    value -> ConfigManager.getConfig().backgroundOpacity = value)
                .controller(opt -> FloatSliderControllerBuilder.create(opt).range(0.1f, 1.0f).step(0.1f))
                .build())
            .option(Option.<Boolean>createBuilder()
                .name(Component.translatable("option.tpshud.backgroundEnabled"))
                .binding(true,
                    () -> ConfigManager.getConfig().backgroundEnabled,
                    value -> ConfigManager.getConfig().backgroundEnabled = value)
                .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                .build())
            .build();
    }
}

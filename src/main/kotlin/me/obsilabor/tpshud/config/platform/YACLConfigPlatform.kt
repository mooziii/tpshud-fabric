package me.obsilabor.tpshud.config.platform

import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder
import dev.isxander.yacl3.api.controller.ColorControllerBuilder
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder
import dev.isxander.yacl3.api.controller.StringControllerBuilder
import dev.isxander.yacl3.gui.controllers.ColorController
import me.obsilabor.tpshud.config.ConfigManager
import me.obsilabor.tpshud.minecraft
import me.obsilabor.tpshud.screen.PositionSelectionScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import java.awt.Color

object YACLConfigPlatform {
    fun buildScreen(parent: Screen?): Screen {
        val builder = YetAnotherConfigLib.createBuilder()
            .title(Text.translatable("title.tpshud.config"))
            .save(ConfigManager::saveConfigToFile)
        val styleCategory = ConfigCategory.createBuilder()
            .name(Text.translatable("category.style"))
            .tooltip(Text.translatable("category.style.tooltip"))
            .group(OptionGroup.createBuilder()
                .name(Text.translatable("category.style.group.appearance"))
                .options(
                    listOf(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("option.tpshud.enabled"))
                            .binding(
                                true,
                                { ConfigManager.configOrException.isEnabled },
                                { ConfigManager.configOrException.isEnabled = it }
                            )
                            .controller { option ->
                                BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true)
                            }.build(),
                        ButtonOption.createBuilder()
                            .name(Text.translatable("option.tpshud.dragHud"))
                            .description(OptionDescription.of(Text.translatable("option.tpshud.dragHud.tooltip")))
                            .action { settingsScreen, _ ->
                                minecraft.setScreen(PositionSelectionScreen(settingsScreen))
                            }
                            .build(),

                        Option.createBuilder<Float>()
                            .name(Text.translatable("option.tpshud.scale"))
                            .binding(
                                1.0F,
                                { ConfigManager.configOrException.scale },
                                { ConfigManager.configOrException.scale = it }
                            )
                            .controller { option ->
                                FloatSliderControllerBuilder.create(option).range(0.1F, 5.0F).step(0.1F)
                            }.build()
                    )
                ).build()
            )
            .group(OptionGroup.createBuilder()
                .name(Text.translatable("category.style.group.text"))
                .options(
                    listOf(
                        Option.createBuilder<Color>()
                            .name(Text.translatable("option.tpshud.textColor"))
                            .binding(
                                Color(16777215),
                                { Color(ConfigManager.configOrException.textColor) },
                                { ConfigManager.configOrException.textColor = it.rgb }
                            )
                            .controller { option ->
                                ColorController(
                                    option,
                                    false
                                )
                                ColorControllerBuilder.create(option).allowAlpha(false)
                            }.build(),
                        Option.createBuilder<Color>()
                            .name(Text.translatable("option.tpshud.valueTextColor"))
                            .binding(
                                Color(8904424),
                                { Color(ConfigManager.configOrException.valueTextColor) },
                                { ConfigManager.configOrException.valueTextColor = it.rgb }
                            )
                            .controller { option ->
                                ColorControllerBuilder.create(option).allowAlpha(false)
                            }.build(),
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("option.tpshud.satisfyTpsCount"))
                            .description(OptionDescription.of(Text.translatable("option.tpshud.satisfyTpsCount.tooltip")))
                            .binding(
                                true,
                                { ConfigManager.configOrException.satisfyTpsCount },
                                { ConfigManager.configOrException.satisfyTpsCount = it }
                            )
                            .controller { option ->
                                BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true)
                            }.build(),
                        Option.createBuilder<String>()
                            .name(Text.translatable("option.tpshud.customText"))
                            .description(OptionDescription.of(Text.translatable("option.tpshud.customText.tooltip")))
                            .binding(
                                "TPS: ",
                                { ConfigManager.configOrException.text },
                                { ConfigManager.configOrException.text = it }
                            )
                            .controller { option ->
                                StringControllerBuilder.create(option)
                            }.build()
                    )
                ).build()
            )
            .group(OptionGroup.createBuilder()
                .name(Text.translatable("category.style.group.background"))
                .options(listOf(
                Option.createBuilder<Color>()
                    .name(Text.translatable("option.tpshud.backgroundColor"))
                    .binding(
                        Color(16777215),
                        { Color(ConfigManager.configOrException.backgroundColor) },
                        { ConfigManager.configOrException.backgroundColor = it.rgb }
                    )
                    .controller { option ->
                        ColorControllerBuilder.create(option).allowAlpha(false)
                    }.build(),
                Option.createBuilder<Float>()
                    .name(Text.translatable("option.tpshud.backgroundOpacity"))
                    .binding(
                        0.5F,
                        { ConfigManager.configOrException.backgroundOpacity },
                        { ConfigManager.configOrException.backgroundOpacity = it }
                    )
                    .controller { option ->
                        FloatSliderControllerBuilder.create(option).range(0.1F, 1.0F).step(0.1F)
                    }.build(),
                Option.createBuilder<Boolean>()
                    .name(Text.translatable("option.tpshud.backgroundEnabled"))
                    .binding(
                        true,
                        { ConfigManager.configOrException.backgroundEnabled },
                        { ConfigManager.configOrException.backgroundEnabled = it }
                    )
                    .controller { option ->
                        BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true)
                    }.build()
            )).build())
        val networkingCategory = ConfigCategory.createBuilder()
            .name(Text.translatable("category.networking"))
            .tooltip(Text.translatable("category.networking.tooltip"))
            .options(listOf(
                Option.createBuilder<Boolean>()
                    .name(Text.translatable("option.tpshud.useServerProvidedData"))
                    .description(OptionDescription.of(Text.translatable("option.tpshud.useServerProvidedData.tooltip1"), Text.translatable("option.tpshud.useServerProvidedData.tooltip2")))
                    .binding(
                        true,
                        { ConfigManager.configOrException.useServerProvidedData ?: true },
                        { ConfigManager.configOrException.useServerProvidedData = it }
                    )
                    .controller { option ->
                        BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true)
                    }.build(),
                Option.createBuilder<Boolean>()
                    .name(Text.translatable("option.tpshud.showCompatibilityToast"))
                    .description(OptionDescription.of(Text.translatable("option.tpshud.showCompatibilityToast.tooltip")))
                    .binding(
                        true,
                        { ConfigManager.configOrException.showCompatibilityToast ?: true },
                        { ConfigManager.configOrException.showCompatibilityToast = it }
                    )
                    .controller { option ->
                        BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true)
                    }.build()
            ))
        builder.categories(listOf(styleCategory.build(), networkingCategory.build()))
        return builder.build().generateScreen(parent)
    }
}
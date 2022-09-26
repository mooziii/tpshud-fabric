package me.obsilabor.tpshud.config.platform

import dev.isxander.yacl.api.ButtonOption
import dev.isxander.yacl.api.ConfigCategory
import dev.isxander.yacl.api.Option
import dev.isxander.yacl.api.OptionGroup
import dev.isxander.yacl.api.YetAnotherConfigLib
import dev.isxander.yacl.gui.controllers.ActionController
import dev.isxander.yacl.gui.controllers.BooleanController
import dev.isxander.yacl.gui.controllers.ColorController
import dev.isxander.yacl.gui.controllers.slider.FloatSliderController
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
                        Option.createBuilder(Boolean::class.java)
                            .name(Text.translatable("option.tpshud.enabled"))
                            .binding(
                                true,
                                { ConfigManager.configOrException.isEnabled },
                                { ConfigManager.configOrException.isEnabled = it }
                            )
                            .controller { option ->
                                BooleanController(
                                    option,
                                    BooleanController.YES_NO_FORMATTER,
                                    true
                                )
                            }.build(),
                        ButtonOption.createBuilder()
                            .name(Text.translatable("option.tpshud.dragHud"))
                            .tooltip(Text.translatable("option.tpshud.dragHud.tooltip"))
                            .action { parent, _ ->
                                minecraft.setScreen(PositionSelectionScreen(parent))
                            }
                            .controller { ActionController(it) }
                            .build(),

                        Option.createBuilder(Float::class.java)
                            .name(Text.translatable("option.tpshud.scale"))
                            .binding(
                                1.0F,
                                { ConfigManager.configOrException.scale },
                                { ConfigManager.configOrException.scale = it }
                            )
                            .controller { option ->
                                FloatSliderController(
                                    option,
                                    0.1F,
                                    5.0F,
                                    0.1F
                                )
                            }.build()
                    )
                ).build()
            )
            .group(OptionGroup.createBuilder()
                .name(Text.translatable("category.style.group.text"))
                .options(
                    listOf(
                        Option.createBuilder(Color::class.java)
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
                            }.build(),
                        Option.createBuilder(Color::class.java)
                            .name(Text.translatable("option.tpshud.valueTextColor"))
                            .binding(
                                Color(8904424),
                                { Color(ConfigManager.configOrException.valueTextColor) },
                                { ConfigManager.configOrException.valueTextColor = it.rgb }
                            )
                            .controller { option ->
                                ColorController(
                                    option,
                                    false
                                )
                            }.build(),
                        Option.createBuilder(Boolean::class.java)
                            .name(Text.translatable("option.tpshud.satisfyTpsCount"))
                            .tooltip(Text.translatable("option.tpshud.satisfyTpsCount.tooltip"))
                            .binding(
                                true,
                                { ConfigManager.configOrException.satisfyTpsCount },
                                { ConfigManager.configOrException.satisfyTpsCount = it }
                            )
                            .controller { option ->
                                BooleanController(
                                    option,
                                    BooleanController.YES_NO_FORMATTER,
                                    true
                                )
                            }.build()
                    )
                ).build()
            )
            .group(OptionGroup.createBuilder()
                .name(Text.translatable("category.style.group.background"))
                .options(listOf(
                Option.createBuilder(Color::class.java)
                    .name(Text.translatable("option.tpshud.backgroundColor"))
                    .binding(
                        Color(16777215),
                        { Color(ConfigManager.configOrException.backgroundColor) },
                        { ConfigManager.configOrException.backgroundColor = it.rgb }
                    )
                    .controller { option ->
                        ColorController(
                            option,
                            false
                        )
                    }.build(),
                Option.createBuilder(Float::class.java)
                    .name(Text.translatable("option.tpshud.backgroundOpacity"))
                    .binding(
                        0.5F,
                        { ConfigManager.configOrException.backgroundOpacity },
                        { ConfigManager.configOrException.backgroundOpacity = it }
                    )
                    .controller { option ->
                        FloatSliderController(
                            option,
                            0.1F,
                            1.0F,
                            0.1F
                        )
                    }.build(),
                Option.createBuilder(Boolean::class.java)
                    .name(Text.translatable("option.tpshud.backgroundEnabled"))
                    .binding(
                        true,
                        { ConfigManager.configOrException.backgroundEnabled },
                        { ConfigManager.configOrException.backgroundEnabled = it }
                    )
                    .controller { option ->
                        BooleanController(
                            option,
                            BooleanController.ON_OFF_FORMATTER,
                            true
                        )
                    }.build()
            )).build())
        val networkingCategory = ConfigCategory.createBuilder()
            .name(Text.translatable("category.networking"))
            .tooltip(Text.translatable("category.networking.tooltip"))
            .options(listOf(
                Option.createBuilder(Boolean::class.java)
                    .name(Text.translatable("option.tpshud.useServerProvidedData"))
                    .tooltip(Text.translatable("option.tpshud.useServerProvidedData.tooltip1"), Text.translatable("option.tpshud.useServerProvidedData.tooltip2"))
                    .binding(
                        true,
                        { ConfigManager.configOrException.useServerProvidedData ?: true },
                        { ConfigManager.configOrException.useServerProvidedData = it }
                    )
                    .controller { option ->
                        BooleanController(
                            option,
                            BooleanController.YES_NO_FORMATTER,
                            true
                        )
                    }.build(),
                Option.createBuilder(Boolean::class.java)
                    .name(Text.translatable("option.tpshud.showCompatibilityToast"))
                    .tooltip(Text.translatable("option.tpshud.showCompatibilityToast.tooltip"))
                    .binding(
                        true,
                        { ConfigManager.configOrException.showCompatibilityToast ?: true },
                        { ConfigManager.configOrException.showCompatibilityToast = it }
                    )
                    .controller { option ->
                        BooleanController(
                            option,
                            BooleanController.YES_NO_FORMATTER,
                            true
                        )
                    }.build()
            ))
        builder.categories(listOf(styleCategory.build(), networkingCategory.build()))
        return builder.build().generateScreen(parent)
    }
}
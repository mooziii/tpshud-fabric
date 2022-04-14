package me.obsilabor.tpshud.config

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.obsilabor.tpshud.json
import me.obsilabor.tpshud.minecraft
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import java.io.File

object ClothConfigManager {

    fun saveConfigToFile() {
        configFile.writeText(json.encodeToString(config))
    }

    fun buildScreen(): Screen {
        val builder = ConfigBuilder.create()
            .setParentScreen(minecraft.currentScreen)
            .setTitle(TranslatableText("title.tpshud.config"))
            .setSavingRunnable {
                saveConfigToFile()
            }
        val general = builder.getOrCreateCategory(TranslatableText("category.tpshud.general"))
        val entryBuilder = builder.entryBuilder()
        general.addEntry(entryBuilder.startBooleanToggle(TranslatableText("option.tpshud.enabled"), config?.isEnabled ?: true)
            .setSaveConsumer {
                config?.isEnabled = it
            }
            .setDefaultValue(true)
            .build())
        general.addEntry(entryBuilder.startFloatField(TranslatableText("option.tpshud.scale"), config?.scale ?: 1f)
            .setSaveConsumer {
                config?.scale = it
            }
            .setDefaultValue(1f)
            .build()
        )
        general.addEntry(entryBuilder.startIntField(Text.of("X Pos: "), config?.x ?: 0)
            .setSaveConsumer {
                config?.x = it
            }
            .setDefaultValue(0)
            .build()
        )
        general.addEntry(entryBuilder.startIntField(Text.of("Y Pos: "), config?.y ?: 0)
            .setSaveConsumer {
                config?.y = it
            }
            .setDefaultValue(0)
            .build()
        )
        general.addEntry(entryBuilder.startColorField(TranslatableText("option.tpshud.textcolor"), config?.textColor ?: 16777215)
            .setSaveConsumer {
                config?.textColor = it
            }
            .setDefaultValue(-1)
            .build()
        )
        general.addEntry(entryBuilder.startColorField(TranslatableText("option.tpshud.valuetextcolor"), config?.valueTextColor ?: 8904424)
            .setSaveConsumer {
                config?.valueTextColor = it
            }
            .setDefaultValue(8904424)
            .build()
        )
        general.addEntry(entryBuilder.startColorField(TranslatableText("option.tpshud.backgroundcolor"), config?.backgroundColor ?: 15458785)
            .setSaveConsumer {
                config?.backgroundColor = it
            }
            .setDefaultValue(15458785)
            .build()
        )
        general.addEntry(entryBuilder.startFloatField(TranslatableText("option.tpshud.backgroundopacity"), config?.backgroundOpacity ?: 0.5f)
            .setSaveConsumer {
                config?.backgroundOpacity = it
            }
            .setDefaultValue(0.5f)
            .build()
        )
        general.addEntry(entryBuilder.startBooleanToggle(TranslatableText("option.tpshud.backgroundenabled"), config?.backgroundEnabled ?: true)
            .setSaveConsumer {
                config?.backgroundEnabled = it
            }
            .setDefaultValue(true)
            .build()
        )
        return builder.build()
    }

    private val configFile = File(System.getProperty("user.dir") + "/config", "tpshud.json")
    var config: TpsHudConfig? = null

    init {
        if(!configFile.parentFile.exists()) {
            configFile.parentFile.mkdirs()
        }
        if(!configFile.exists()) {
            configFile.createNewFile()
            configFile.writeText(json.encodeToString(TpsHudConfig(
                true,
                0,
                0,
                16777215,
                8904424,
                1f,
                16777215,
                0.5f,
                true
            )))
        }
        kotlin.runCatching {
            config = json.decodeFromString(configFile.readText())
        }
    }

}
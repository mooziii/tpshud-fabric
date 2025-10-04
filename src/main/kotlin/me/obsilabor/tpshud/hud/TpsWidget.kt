@file:Suppress("unused")

package me.obsilabor.tpshud.hud

import me.obsilabor.tpshud.TpsTracker
import me.obsilabor.tpshud.config.ConfigManager
import me.obsilabor.tpshud.minecraft
import net.minecraft.client.gui.DrawContext
import java.awt.Color
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

object TpsWidget {
    fun render(context: DrawContext) {
        val config = ConfigManager.config ?: return
        if(!config.isEnabled) return
        val matrices = context.matrices
        matrices.scale(ConfigManager.config?.scale?:1f, ConfigManager.config?.scale?:1f, matrices)
        if(config.backgroundEnabled) {
            fillBackground(context, config.x.toFloat(), config.y.toFloat(), config.x+width.toFloat(), config.y+minecraft.textRenderer.fontHeight.toFloat()/*+1f*/, config.backgroundColor, config.backgroundOpacity)
        }

        // Get text and value, for MSPT or TPS
        var (text, value) = getTextAndValue()

        // Render it
        val widthPartOne = minecraft.textRenderer.getWidth(text)
        context.drawText(minecraft.textRenderer, text, config.x, config.y, config.textColor, config.textShadow)
        context.drawText(minecraft.textRenderer, value, config.x+widthPartOne, config.y, config.valueTextColor, config.textShadow)
    }

    private fun getTextAndValue(): Pair<String, String> {
    val config = ConfigManager.config ?: return Pair("TPS: ", "0.00")

    val rawValue = TpsTracker.INSTANCE.tickTime
    var (text, value) = if (config.displayModeTps) {
        Pair("TPS: ", convertToTps(rawValue))
    } else {
        Pair("MSPT: ", rawValue)
    }

    if (!config.customText.isEmpty()) {
        text = config.customText
    }

    // Fallback in case of NaN or weird values
    val safeValue = if (value.isNaN() || value.isInfinite()) {
        0.0f
    } else {
        value
    }

    val valueStr = if (config.satisfyTpsCount) {
        safeValue.roundToInt().toString()
    } else {
        BigDecimal(safeValue.toDouble()).setScale(2, RoundingMode.HALF_UP).toFloat().toString()
    }

    return Pair(text, valueStr)
}


    private fun convertToTps(mspt: Float): Float {
        return kotlin.math.min(1000f / mspt, 20f);
    }

    val width: Int
        get() {
            val (text, value) = getTextAndValue()
            return minecraft.textRenderer.getWidth(text) + minecraft.textRenderer.getWidth(value)
        }

    private fun fillBackground(drawContext: DrawContext, x1: Float, y1: Float, x2: Float, y2: Float, color: Int, alpha: Float) {
        val rgb = Color(color)
        drawContext.fill(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt(), Color(rgb.red, rgb.blue, rgb.green, (alpha*255).roundToInt()).rgb)
    }
}